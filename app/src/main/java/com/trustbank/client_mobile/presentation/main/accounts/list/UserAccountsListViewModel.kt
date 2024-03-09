package com.trustbank.client_mobile.presentation.main.accounts.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.domain.LoanRepository
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.Loan
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UserAccountsListViewModel(
    private val accountRepository: AccountRepository,
    private val loanRepository: LoanRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserInfoUiState> = MutableStateFlow(UserInfoUiState())

    val uiState = _uiState.asStateFlow()

    private var updatingJob: Job? = null

    init {
        refreshAccountsList()
    }

    fun refreshAccountsList() {
        updatingJob?.let { existingJob ->
            if (existingJob.isActive)
                existingJob.cancel()
        }

        updatingJob = viewModelScope.launch {
            _uiState.value = _uiState.value.copy(accounts = emptyList(), loans = emptyList())


            coroutineScope {
                launch {
                    accountRepository.getAccounts().collect { account ->
                        account.onSuccess {
                            val currentAccounts = _uiState.value.accounts
                            _uiState.value = _uiState.value.copy(accounts = currentAccounts + it)
                        }

                        // TODO: handle error (скорее не произойдёт)
                    }
                }
            }

            coroutineScope {
                launch {
                    loanRepository.getLoans().collect { loan ->
                        loan.onSuccess {
                            val currentLoans = _uiState.value.loans
                            _uiState.value = _uiState.value.copy(loans = currentLoans + it)
                        }
                    }
                }
            }
        }
    }

    fun openNewAccount() {
        viewModelScope.launch {
            accountRepository.openNewAccount().first().onSuccess {
                refreshAccountsList()
            }
        }
    }
}


data class UserInfoUiState(
    val accounts: List<Account> = emptyList(),
    val loans: List<Loan> = emptyList()
)
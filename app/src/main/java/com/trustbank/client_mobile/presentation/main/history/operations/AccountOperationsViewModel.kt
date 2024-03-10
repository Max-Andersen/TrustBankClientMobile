package com.trustbank.client_mobile.presentation.main.history.operations

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.presentation.main.accounts.list.UserInfoUiState
import com.trustbank.client_mobile.proto.AccountType
import com.trustbank.client_mobile.proto.Transaction
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AccountOperationsViewModel(
    savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository
): ViewModel() {
    private val accountId = requireNotNull(savedStateHandle.get<String>("accountId"))

    private val _uiState: MutableStateFlow<List<Transaction>> = MutableStateFlow(emptyList())

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
            _uiState.value = emptyList()


            coroutineScope {
                launch {
                    accountRepository.getHistoryOfAccount(accountId, 0, 199).collect { account ->
                        account.onSuccess {
                            _uiState.value = it.elementsList
                        }

                        // TODO: handle error (скорее не произойдёт)
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
package com.trustbank.client_mobile.presentation.main.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.proto.Account
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AccountsHistoryViewModel(
    private val accountRepository: AccountRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<List<Account>> = MutableStateFlow(emptyList())

    val uiState = _uiState.asStateFlow()

    private var updatingJob: Job? = null

    init {
        refreshTransactionList()
    }

    fun refreshTransactionList() {
        updatingJob?.let { existingJob ->
            if (existingJob.isActive)
                existingJob.cancel()
        }

        updatingJob = viewModelScope.launch {
            _uiState.value = emptyList()


            coroutineScope {
                launch {
                    accountRepository.getAccounts().collect { account ->
                        Log.d("AccountsHistoryViewModel", "account: $account")
                        account.onSuccess {
                            val currentAccounts = _uiState.value
                            _uiState.value = currentAccounts + it
                        }

                        // TODO: handle error (скорее не произойдёт)
                    }
                }
            }
        }
    }
}
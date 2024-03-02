package com.trustbank.client_mobile.presentation.main.info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.proto.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserInfoViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<UserInfoUiState> = MutableStateFlow(UserInfoUiState())

    val uiState = _uiState.asStateFlow()

    init {
        refreshAccountsList()
    }

    fun refreshAccountsList() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(accounts = emptyList())
            accountRepository.getAccounts().collect { account ->
                account.onSuccess {
                    val currentAccounts = _uiState.value.accounts
                    _uiState.value = _uiState.value.copy(accounts = currentAccounts + it)
                }

                // TODO: handle error (скорее не произойдёт)
            }
        }
    }
}


data class UserInfoUiState(
    val accounts: List<Account> = emptyList(),
)
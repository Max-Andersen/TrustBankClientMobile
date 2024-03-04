package com.trustbank.client_mobile.presentation.main.accounts.card

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.proto.Account
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AccountCardViewModel(
    savedStateHandle: SavedStateHandle,
    private val accountRepository: AccountRepository
) : ViewModel() {
    val id = requireNotNull(savedStateHandle.get<String>("id"))

    private var accountData: MutableStateFlow<Account?> = MutableStateFlow(null)

    val account = accountData.asStateFlow()

    init {
        viewModelScope.launch {
            accountRepository.getAccount().first().onSuccess {
                accountData.emit(it)
            }.onFailure {
                Log.d("AccountCardViewModel", "Error when load account data: $it")
            }
        }
    }
}
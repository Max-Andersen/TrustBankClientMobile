package com.trustbank.client_mobile.presentation.main.accounts.card

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.proto.Account
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _effects: MutableSharedFlow<AccountCardEffect> = MutableSharedFlow()

    val effects = _effects.asSharedFlow()

    init {
        updateAccountData()
    }

    private fun updateAccountData() {
        viewModelScope.launch {
            accountRepository.getAccount(accountId = id).first().onSuccess {
                accountData.emit(it)
            }.onFailure {
                Log.d("AccountCardViewModel", "Error when load account data: $it")
            }
        }
    }

    fun closeAccount() {
        viewModelScope.launch {
            accountRepository.closeAccount(accountId = id).first().onSuccess {
                _effects.emit(AccountCardEffect.NavigateBack)
            }.onFailure {
                Log.d("AccountCardViewModel", "Error when delete account: $it")
            }
        }
    }

    fun deposit(amount: Double) {
        viewModelScope.launch {
            accountRepository.depositMoney(accountId = id, amount = (amount * 100).toLong()).first()
                .onSuccess {
                    updateAccountData()
                }.onFailure {
                Log.d("AccountCardViewModel", "Error when deposit money: $it")
            }
        }
    }

    fun withdraw(amount: Double) {
        viewModelScope.launch {
            accountRepository.withdrawMoney(accountId = id, amount = (amount * 100).toLong())
                .first().onSuccess {
                updateAccountData()
            }.onFailure {
                Log.d("AccountCardViewModel", "Error when withdraw money: $it")
            }
        }
    }

    fun transfer(amount: Double, toAccountId: String) {
        viewModelScope.launch {
            accountRepository.transferMoney(
                fromAccountId = id,
                toAccountId = toAccountId,
                amount = (amount * 100).toLong()
            ).first().onSuccess {
                updateAccountData()
            }.onFailure {
                Log.d("AccountCardViewModel", "Error when transfer money: $it")
            }
        }
    }
}

sealed class AccountCardEffect {
    data object NavigateBack : AccountCardEffect()

}

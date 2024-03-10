package com.trustbank.client_mobile.presentation.main.accounts.card

import android.util.Log
import android.view.View
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.domain.LoanRepository
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.Loan
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class CreditViewModel(
    savedStateHandle: SavedStateHandle,
    private val loanRepository: LoanRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {
    private val loanId = requireNotNull(savedStateHandle.get<String>("id"))

    private var accountId: String? = null

    private var accountData: MutableStateFlow<Pair<Loan, Account>?> = MutableStateFlow(null)

    val account = accountData.asStateFlow()

    private val _effects: MutableSharedFlow<CreditAccountCardEffect> = MutableSharedFlow()

    val effects = _effects.asSharedFlow()

    init {
        updateAccountData()
    }

    private fun updateAccountData() {
        viewModelScope.launch {
            loanRepository.getLoanInfo(loanId = loanId).first().onSuccess { loan ->
                accountId = loan.accountId
                accountRepository.getAccount(accountId = loan.accountId).first()
                    .onSuccess { account ->
                        accountData.emit(loan to account)
                    }.onFailure {
                        Log.d("AccountCardViewModel", "Error when load account data: $it")
                    }
            }.onFailure {
                Log.d("CreditViewModel", "Error when load loan data: $it")
            }
        }
    }

    fun deposit(amount: Double) {
        viewModelScope.launch {
            accountId?.let {
                accountRepository.depositMoney(accountId = it, amount = (amount * 100).toLong()).first()
                    .onSuccess {
                        updateAccountData()
                    }.onFailure {
                        Log.d("AccountCardViewModel", "Error when deposit money: $it")
                    }
            }
        }
    }

    fun withdraw(amount: Double) {
        viewModelScope.launch {
            accountId?.let {
                accountRepository.withdrawMoney(accountId = it, amount = (amount * 100).toLong())
                    .first().onSuccess {
                        updateAccountData()
                    }.onFailure {
                        Log.d("AccountCardViewModel", "Error when withdraw money: $it")
                    }
            }
        }
    }

    fun transfer(amount: Double, toAccountId: String) {
        viewModelScope.launch {
            accountId?.let {
                accountRepository.transferMoney(
                    fromAccountId = it,
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


}

sealed class CreditAccountCardEffect {
    data object NavigateBack : CreditAccountCardEffect()
}
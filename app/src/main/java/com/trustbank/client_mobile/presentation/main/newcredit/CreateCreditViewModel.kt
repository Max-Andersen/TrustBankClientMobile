package com.trustbank.client_mobile.presentation.main.newcredit

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.asDoubleState
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.LoanRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class CreateCreditViewModel(
    savedStateHandle: SavedStateHandle,
    private val loanRepository: LoanRepository
) : ViewModel() {
    private val tariffId = requireNotNull(savedStateHandle.get<String>("tariffId"))
    private val interestRate = requireNotNull(savedStateHandle.get<String>("interestRate"))

    private var _issuedAmount: MutableState<Double> = mutableDoubleStateOf(0.0)
    private var _loanTermInDays: MutableState<Int> = mutableIntStateOf(0)


    val issuedAmount = _issuedAmount.asDoubleState()
    val loanTermInDays = _loanTermInDays.asIntState()



    private val _effect = MutableSharedFlow<CreateCreditEffect>()
    val effect: SharedFlow<CreateCreditEffect> = _effect.asSharedFlow()

    fun setIssuedAmount(amount: Double) {
        _issuedAmount.value = amount
    }

    fun setDays(days: Int) {
        _loanTermInDays.value = days
    }


    fun createCredit() {
        viewModelScope.launch {
            loanRepository.createLoan(
                tariffId = tariffId,
                issuedAmount = _issuedAmount.value,
                loanTermInDays = _loanTermInDays.value
            ).collect {
                if (it.isSuccess)
                    _effect.emit(CreateCreditEffect.Success)
                else
                    _effect.emit(CreateCreditEffect.Fail)
            }
        }

    }
}

sealed class CreateCreditEffect {
    data object Success : CreateCreditEffect()
    data object Fail : CreateCreditEffect()
}
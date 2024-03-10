package com.trustbank.client_mobile.presentation.main.accounts.card

import android.view.View
import androidx.lifecycle.ViewModel
import com.trustbank.client_mobile.domain.LoanRepository

class CreditViewModel(
    private val loanRepository: LoanRepository,
    private val coreRepository: LoanRepository
): ViewModel() {

}
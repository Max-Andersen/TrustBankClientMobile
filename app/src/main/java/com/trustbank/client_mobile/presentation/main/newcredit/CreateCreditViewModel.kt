package com.trustbank.client_mobile.presentation.main.newcredit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class CreateCreditViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val tariffId = requireNotNull(savedStateHandle.get<String>("tariffId"))


}
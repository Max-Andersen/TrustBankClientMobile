package com.trustbank.client_mobile.presentation.main.tariffs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.LoanRepository
import com.trustbank.client_mobile.proto.LoanTariff
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TariffListViewModel(
    private val loanRepository: LoanRepository,
) : ViewModel() {


    private val _tariffList = MutableStateFlow<List<LoanTariff>>(emptyList())


    val tariffList = _tariffList.asStateFlow()

    private var updatingJob: Job? = null


    init {
        viewModelScope.launch {
            refreshTariffs()
        }
    }

    suspend fun refreshTariffs() {
        updatingJob?.let { existingJob ->
            if (existingJob.isActive)
                existingJob.cancel()
        }

        updatingJob = viewModelScope.launch {
            _tariffList.value = listOf()


            loanRepository.getLoanTariffs().collect { account ->
                account.onSuccess {
                    val currentAccounts = _tariffList.value
                    _tariffList.value = currentAccounts + it
                }

                // TODO: handle error (скорее не произойдёт)
            }
        }
    }

}
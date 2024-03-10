package com.trustbank.client_mobile.presentation.main.loancreation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.domain.LoanRepository
import com.trustbank.client_mobile.proto.LoanRequest
import com.trustbank.client_mobile.proto.LoanTariff
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TariffListViewModel(
    private val loanRepository: LoanRepository,
) : ViewModel() {


    private val _tariffList = MutableStateFlow<List<LoanTariff>>(emptyList())
    private val _loanRequestsList = MutableStateFlow<List<LoanRequest>>(emptyList())


    val tariffList = _tariffList.asStateFlow()
    val loanRequestsList = _loanRequestsList.asStateFlow()

    private var updatingJob: Job? = null


    init {
        viewModelScope.launch {
            refreshData()
        }
    }

    suspend fun refreshData() {
        updatingJob?.let { existingJob ->
            if (existingJob.isActive)
                existingJob.cancel()
        }

        updatingJob = viewModelScope.launch {
            _tariffList.value = listOf()
            _loanRequestsList.value = listOf()

            coroutineScope {
                launch {
                    loanRepository.getLoanTariffs().collect { account ->
                        account.onSuccess {
                            val currentAccounts = _tariffList.value
                            _tariffList.value = currentAccounts + it
                        }

                        // TODO: handle error (скорее не произойдёт)
                    }
                }
                launch {
                    Log.d("UPDATE_LOAN", "start")
                    loanRepository.getLoanRequests().collect { loanRequest ->
                        loanRequest.onSuccess {
                            val currentAccounts = _loanRequestsList.value
                            _loanRequestsList.value = currentAccounts + it
                        }
                        Log.d("UPDATE_LOAN", loanRequest.toString())

                        // TODO: handle error (скорее не произойдёт)
                    }
                }
            }
        }
    }

}
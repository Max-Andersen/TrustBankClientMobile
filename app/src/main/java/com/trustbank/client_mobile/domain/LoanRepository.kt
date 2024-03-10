package com.trustbank.client_mobile.domain

import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.AccountOperationServiceGrpc
import com.trustbank.client_mobile.proto.CreateLoanRequestRequest
import com.trustbank.client_mobile.proto.GetAccountsRequest
import com.trustbank.client_mobile.proto.GetClientLoansRequest
import com.trustbank.client_mobile.proto.GetLoanByIdRequest
import com.trustbank.client_mobile.proto.GetLoanRequestRequest
import com.trustbank.client_mobile.proto.GetLoanTariffsRequest
import com.trustbank.client_mobile.proto.Loan
import com.trustbank.client_mobile.proto.LoanOperationServiceGrpc
import com.trustbank.client_mobile.proto.LoanRequest
import com.trustbank.client_mobile.proto.LoanTariff
import com.trustbank.client_mobile.proto.ShortLoanInfo
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class LoanRepository(
    private val grpcService: LoanOperationServiceGrpc.LoanOperationServiceStub
) : BaseRepository() {

    fun getLoanTariffs(): SharedFlow<Result<LoanTariff>> {
        val request = GetLoanTariffsRequest.newBuilder()
            .build()
        val responseFlow: MutableSharedFlow<Result<LoanTariff>> = MutableSharedFlow()
        val observer = getObserver<GetLoanTariffsRequest, LoanTariff>(responseFlow)
        grpcService.getLoanTariffs(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun getLoans(): SharedFlow<Result<ShortLoanInfo>> {
        val request = GetClientLoansRequest.newBuilder()
            .build()
        val responseFlow: MutableSharedFlow<Result<ShortLoanInfo>> = MutableSharedFlow()
        val observer = getObserver<GetClientLoansRequest, ShortLoanInfo>(responseFlow)
        grpcService.getLoans(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun getLoanRequests(): SharedFlow<Result<LoanRequest>> {
        val request = GetLoanRequestRequest.newBuilder()
            .build()
        val responseFlow: MutableSharedFlow<Result<LoanRequest>> = MutableSharedFlow()
        val observer = getObserver<GetLoanRequestRequest, LoanRequest>(responseFlow)
        grpcService.getLoanRequests(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun getLoanInfo(loanId: String): SharedFlow<Result<Loan>> {
        val request = GetLoanByIdRequest.newBuilder()
            .setId(loanId)
            .build()
        val responseFlow: MutableSharedFlow<Result<Loan>> = MutableSharedFlow()
        val observer = getObserver<GetClientLoansRequest, Loan>(responseFlow)
        grpcService.getLoanById(request, observer)
        return responseFlow.asSharedFlow()
    }


    fun createLoan(tariffId: String, issuedAmount: Double, loanTermInDays: Int, ): SharedFlow<Result<LoanRequest>> {
        val request = CreateLoanRequestRequest.newBuilder()
            .setIssuedAmount((issuedAmount * 100L).toLong())
            .setLoanTermInDays(loanTermInDays)
            .setTariffId(tariffId)
            .build()
        val responseFlow: MutableSharedFlow<Result<LoanRequest>> = MutableSharedFlow()
        val observer = getObserver<CreateLoanRequestRequest, LoanRequest>(responseFlow)
        grpcService.createLoanRequest(request, observer)
        return responseFlow.asSharedFlow()
    }


}
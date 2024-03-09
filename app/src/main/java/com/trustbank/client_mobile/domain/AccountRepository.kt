package com.trustbank.client_mobile.domain

import android.util.Log
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.AccountOperationServiceGrpc
import com.trustbank.client_mobile.proto.Client
import com.trustbank.client_mobile.proto.CloseAccountRequest
import com.trustbank.client_mobile.proto.GetAccountInfoRequest
import com.trustbank.client_mobile.proto.GetAccountsRequest
import com.trustbank.client_mobile.proto.GetHistoryOfAccountRequest
import com.trustbank.client_mobile.proto.LoginRequest
import com.trustbank.client_mobile.proto.MoneyOperation
import com.trustbank.client_mobile.proto.OpenAccountRequest
import com.trustbank.client_mobile.proto.OperationResponse
import com.trustbank.client_mobile.proto.TransactionHistoryPage
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking

class AccountRepository(
    private val grpcService: AccountOperationServiceGrpc.AccountOperationServiceStub
): BaseRepository() {

    fun getAccounts(): SharedFlow<Result<Account>> {
        val request = GetAccountsRequest.newBuilder()
            .build()
        val responseFlow: MutableSharedFlow<Result<Account>> = MutableSharedFlow()
        val observer = getObserver<GetAccountsRequest, Account>(responseFlow)
        grpcService.getAccounts(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun getAccount(accountId: String): SharedFlow<Result<Account>> {
        val request = GetAccountInfoRequest.newBuilder()
            .setAccountId(accountId)
            .build()
        val responseFlow: MutableSharedFlow<Result<Account>> = MutableSharedFlow()
        val observer = getObserver<GetAccountInfoRequest, Account>(responseFlow)
        grpcService.getAccountInfo(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun openNewAccount(): SharedFlow<Result<OperationResponse>> {
        val request = OpenAccountRequest.newBuilder()
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<GetAccountsRequest, OperationResponse>(responseFlow)
        grpcService.openNewAccount(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun closeAccount(accountId: String): SharedFlow<Result<OperationResponse>> {
        val request = CloseAccountRequest.newBuilder()
            .setAccountId(accountId)
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<CloseAccountRequest, OperationResponse>(responseFlow)
        grpcService.closeAccount(request, observer)
        return responseFlow
    }

    fun depositMoney(
        accountId: String,
        amount: Long
    ): SharedFlow<Result<OperationResponse>> {
        val request = MoneyOperation.newBuilder()
            .setAccountId(accountId)
            .setAmount(amount)
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<CloseAccountRequest, OperationResponse>(responseFlow)
        grpcService.depositMoney(request, observer)
        return responseFlow
    }

    fun withdrawMoney(
        accountId: String,
        amount: Long
    ): SharedFlow<Result<OperationResponse>> {
        val request = MoneyOperation.newBuilder()
            .setAccountId(accountId)
            .setAmount(amount)
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<CloseAccountRequest, OperationResponse>(responseFlow)
        grpcService.withdrawMoney(request, observer)
        return responseFlow
    }

    fun getHistoryOfAccount(
        accountId: String,
        pageNumber: Int,
        pageSize: Int
    ): SharedFlow<Result<TransactionHistoryPage>> {
        val request = GetHistoryOfAccountRequest.newBuilder()
            .setAccountId(accountId)
            .setPageNumber(pageNumber)
            .setPageSize(pageSize)
            .build()
        val responseFlow: MutableSharedFlow<Result<TransactionHistoryPage>> = MutableSharedFlow()
        val observer = getObserver<GetHistoryOfAccountRequest, TransactionHistoryPage>(responseFlow)
        grpcService.getHistoryOfAccount(request, observer)
        return responseFlow
    }


}
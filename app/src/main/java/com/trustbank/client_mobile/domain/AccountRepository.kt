package com.trustbank.client_mobile.domain

import android.util.Log
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.AccountOperationsServiceGrpc
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
    private val grpcService: AccountOperationsServiceGrpc.AccountOperationsServiceStub
) {
    private fun <T, U> getObserver(
        resultFlow: MutableSharedFlow<Result<U>>,
        onNext: ((U) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onCompleted: () -> Unit = {}
    ): ClientResponseObserver<T, U> {
        return object : ClientResponseObserver<T, U> {
            override fun beforeStart(requestStream: ClientCallStreamObserver<T>?) {}

            override fun onNext(value: U) {
                Log.d("Grpc", "got data $value")
                onNext?.let {
                    it(value)
                } ?: runBlocking {
                    resultFlow.emit(Result.success(value))
                }
            }

            override fun onError(t: Throwable?) {
                Log.d("Grpc", "request failed with error $t")
                onError?.let {
                    it(t!!)
                } ?: runBlocking {
                    resultFlow.emit(Result.failure(t!!))
                }
            }

            override fun onCompleted() {
                Log.d("Grpc", "request completed")
                onCompleted()
            }
        }
    }


    fun login(login: String, password: String): SharedFlow<Result<Client>> {
        val request = LoginRequest.newBuilder()
            .setLogin(login)
            .setPassword(password)
            .build()
        val responseFlow: MutableSharedFlow<Result<Client>> = MutableSharedFlow()
        val observer = getObserver<LoginRequest, Client>(responseFlow)
        grpcService.login(request, observer)
        return responseFlow.asSharedFlow()
    }


    fun getAccounts(userId: String = "173ea10f-0915-4c47-a8a3-d293f0aa24bc"): SharedFlow<Result<Account>> {
        val request = GetAccountsRequest.newBuilder()
            .setUserId(userId)
            .build()
        val responseFlow: MutableSharedFlow<Result<Account>> = MutableSharedFlow()
        val observer = getObserver<GetAccountsRequest, Account>(responseFlow)
        grpcService.getAccounts(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun getAccount(userId: String = "173ea10f-0915-4c47-a8a3-d293f0aa24bc", accountId: String): SharedFlow<Result<Account>> {
        val request = GetAccountInfoRequest.newBuilder()
            .setAccountId(accountId)
            .build()
        val responseFlow: MutableSharedFlow<Result<Account>> = MutableSharedFlow()
        val observer = getObserver<GetAccountInfoRequest, Account>(responseFlow)
        grpcService.getAccountInfo(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun openNewAccount(userId: String = "173ea10f-0915-4c47-a8a3-d293f0aa24bc"): SharedFlow<Result<OperationResponse>> {
        val request = OpenAccountRequest.newBuilder()
            .setUserId(userId)
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<GetAccountsRequest, OperationResponse>(responseFlow)
        grpcService.openNewAccount(request, observer)
        return responseFlow.asSharedFlow()
    }

    fun closeAccount(userId: String = "173ea10f-0915-4c47-a8a3-d293f0aa24bc", accountId: String): SharedFlow<Result<OperationResponse>> {
        val request = CloseAccountRequest.newBuilder()
            .setAccountId(accountId)
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<CloseAccountRequest, OperationResponse>(responseFlow)
        grpcService.closeAccount(request, observer)
        return responseFlow
    }

    fun depositMoney(
        userId: String,
        accountId: String,
        amount: Long
    ): SharedFlow<Result<OperationResponse>> {
        val request = MoneyOperation.newBuilder()
            .setUserId(accountId)
            .setAmount(amount)
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<CloseAccountRequest, OperationResponse>(responseFlow)
        grpcService.depositMoney(request, observer)
        return responseFlow
    }

    fun withdrawMoney(
        userId: String,
        accountId: String,
        amount: Long
    ): SharedFlow<Result<OperationResponse>> {
        val request = MoneyOperation.newBuilder()
            .setUserId(accountId)
            .setAmount(amount)
            .build()
        val responseFlow: MutableSharedFlow<Result<OperationResponse>> = MutableSharedFlow()
        val observer = getObserver<CloseAccountRequest, OperationResponse>(responseFlow)
        grpcService.withdrawMoney(request, observer)
        return responseFlow
    }

    fun getHistoryOfAccount(
        userId: String,
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
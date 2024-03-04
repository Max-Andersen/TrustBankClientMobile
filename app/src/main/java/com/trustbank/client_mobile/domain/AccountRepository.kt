package com.trustbank.client_mobile.domain

import android.util.Log
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.AccountOperationsServiceGrpc
import com.trustbank.client_mobile.proto.Client
import com.trustbank.client_mobile.proto.CloseAccountRequest
import com.trustbank.client_mobile.proto.GetAccountsRequest
import com.trustbank.client_mobile.proto.GetHistoryOfAccountRequest
import com.trustbank.client_mobile.proto.LoginRequest
import com.trustbank.client_mobile.proto.MoneyOperation
import com.trustbank.client_mobile.proto.OpenAccountRequest
import com.trustbank.client_mobile.proto.OperationResponse
import com.trustbank.client_mobile.proto.TransactionHistoryPage
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

class AccountRepository(
    private val grpcService: AccountOperationsServiceGrpc.AccountOperationsServiceStub
) {
    private fun <T, U> getObserver(
        onNext: (U) -> Unit,
        onError: (Throwable) -> Unit,
        onCompleted: () -> Unit = {}
    ): ClientResponseObserver<T, U> {
        return object : ClientResponseObserver<T, U> {
            override fun beforeStart(requestStream: ClientCallStreamObserver<T>?) {}

            override fun onNext(value: U) {
                Log.d("Grpc", "got data $value")
                onNext(value)
            }

            override fun onError(t: Throwable?) {
                Log.d("Grpc", "request failed with error $t")
                onError(t ?: Throwable("Unknown error"))
            }

            override fun onCompleted() {
                Log.d("Grpc", "request completed")
                onCompleted()
            }
        }
    }


    fun login(login: String, password: String): Result<Client> {
        val request = LoginRequest.newBuilder()
            .setLogin(login)
            .setPassword(password)
            .build()
        var response: Result<Client> = Result.failure(Throwable("Unknown error"))

        val observer = getObserver<LoginRequest, Client>(
            onNext = { client -> response = Result.success(client) },
            onError = { error -> response = Result.failure(error) },
        )

        grpcService.login(request, observer)
        return response
    }


    suspend fun getAccounts(userId: String = "173ea10f-0915-4c47-a8a3-d293f0aa24bc"): SharedFlow<Result<Account>> {
        val request = GetAccountsRequest.newBuilder()
            .setUserId(userId)
            .build()
        val responseFlow: MutableSharedFlow<Result<Account>> = MutableSharedFlow()

        val observer = getObserver<LoginRequest, Account>(
            onNext = { client -> runBlocking { responseFlow.emit(Result.success(client)) } },
            onError = { error -> runBlocking { responseFlow.emit(Result.failure(error)) } },
        )

        grpcService.getAccounts(request, observer)


        return responseFlow.asSharedFlow()
    }

    suspend fun getAccount(userId: String = "173ea10f-0915-4c47-a8a3-d293f0aa24bc"): SharedFlow<Result<Account>> {
        val request = GetAccountsRequest.newBuilder()
            .setUserId(userId)
            .build()
        val responseFlow: MutableSharedFlow<Result<Account>> = MutableSharedFlow()

        val observer = getObserver<LoginRequest, Account>(
            onNext = { client -> runBlocking { responseFlow.emit(Result.success(client)) } },
            onError = { error -> runBlocking { responseFlow.emit(Result.failure(error)) } },
        )

        grpcService.getAccounts(request, observer)

        return responseFlow.asSharedFlow()
    }

    fun openNewAccount(userId: String): Result<OperationResponse> {
        val request = OpenAccountRequest.newBuilder()
            .setUserId(userId)
            .build()
        var response: Result<OperationResponse> = Result.failure(Throwable("Unknown error"))

        val observer = getObserver<GetAccountsRequest, OperationResponse>(
            onNext = { client -> response = Result.success(client) },
            onError = { error -> response = Result.failure(error) },
        )

        grpcService.openNewAccount(request, observer)
        return response
    }

    fun closeAccount(userId: String, accountId: String): Result<OperationResponse> {
        val request = CloseAccountRequest.newBuilder()
            .setUserId(accountId)
            .build()
        var response: Result<OperationResponse> = Result.failure(Throwable("Unknown error"))

        val observer = getObserver<CloseAccountRequest, OperationResponse>(
            onNext = { client -> response = Result.success(client) },
            onError = { error -> response = Result.failure(error) },
        )

        grpcService.closeAccount(request, observer)
        return response
    }

    fun depositMoney(userId: String, accountId: String, amount: Long): Result<OperationResponse> {
        val request = MoneyOperation.newBuilder()
            .setUserId(accountId)
            .setAmount(amount)
            .build()
        var response: Result<OperationResponse> = Result.failure(Throwable("Unknown error"))

        val observer = getObserver<CloseAccountRequest, OperationResponse>(
            onNext = { client -> response = Result.success(client) },
            onError = { error -> response = Result.failure(error) },
        )

        grpcService.depositMoney(request, observer)
        return response
    }

    fun withdrawMoney(userId: String, accountId: String, amount: Long): Result<OperationResponse> {
        val request = MoneyOperation.newBuilder()
            .setUserId(accountId)
            .setAmount(amount)
            .build()
        var response: Result<OperationResponse> = Result.failure(Throwable("Unknown error"))

        val observer = getObserver<CloseAccountRequest, OperationResponse>(
            onNext = { client -> response = Result.success(client) },
            onError = { error -> response = Result.failure(error) },
        )

        grpcService.withdrawMoney(request, observer)
        return response
    }

    fun getHistoryOfAccount(
        userId: String,
        accountId: String,
        pageNumber: Int,
        pageSize: Int
    ): Result<TransactionHistoryPage> {
        val request = GetHistoryOfAccountRequest.newBuilder()
            .setAccountId(accountId)
            .setPageNumber(pageNumber)
            .setPageSize(pageSize)
            .build()
        var response: Result<TransactionHistoryPage> = Result.failure(Throwable("Unknown error"))

        val observer = getObserver<GetHistoryOfAccountRequest, TransactionHistoryPage>(
            onNext = { client -> response = Result.success(client) },
            onError = { error -> response = Result.failure(error) },
        )

        grpcService.getHistoryOfAccount(request, observer)
        return response
    }


}
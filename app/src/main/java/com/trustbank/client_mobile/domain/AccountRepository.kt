package com.trustbank.client_mobile.domain

import android.util.Log
import com.trustbank.client_mobile.proto.Account
import com.trustbank.client_mobile.proto.AccountOperationsServiceGrpc
import com.trustbank.client_mobile.proto.Client
import com.trustbank.client_mobile.proto.GetAccountsRequest
import com.trustbank.client_mobile.proto.LoginRequest
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


    suspend fun getAccounts(userId: String = "999"): SharedFlow<Result<Account>> {
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


}
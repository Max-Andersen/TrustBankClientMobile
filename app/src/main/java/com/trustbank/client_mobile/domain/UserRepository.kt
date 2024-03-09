package com.trustbank.client_mobile.domain

import com.trustbank.client_mobile.proto.AccountOperationServiceGrpc
import com.trustbank.client_mobile.proto.Client
import com.trustbank.client_mobile.proto.LoginRequest
import com.trustbank.client_mobile.proto.UserOperationServiceGrpc
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class UserRepository(
    private val grpcService: UserOperationServiceGrpc.UserOperationServiceStub
): BaseRepository() {
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
}
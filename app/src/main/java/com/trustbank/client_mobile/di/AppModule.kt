package com.trustbank.client_mobile.di

import android.net.Uri
import com.trustbank.client_mobile.localClientDataPreferences
import com.trustbank.client_mobile.data.HeaderClientInterceptor
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.domain.UserRepository
import com.trustbank.client_mobile.domain.usecase.LoginUseCase
import com.trustbank.client_mobile.presentation.authorization.login.LoginViewModel
import com.trustbank.client_mobile.presentation.authorization.register.RegisterViewModel
import com.trustbank.client_mobile.presentation.main.accounts.card.AccountCardViewModel
import com.trustbank.client_mobile.presentation.main.accounts.list.UserAccountsListViewModel
import com.trustbank.client_mobile.proto.AccountOperationServiceGrpc
import com.trustbank.client_mobile.proto.AccountOperationServiceGrpc.AccountOperationServiceStub
import com.trustbank.client_mobile.proto.UserOperationServiceGrpc
import com.trustbank.client_mobile.proto.UserOperationServiceGrpc.UserOperationServiceStub
import io.grpc.ClientInterceptor
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

private const val DATASTORE_LOCAL_CLIENT_DATA = "datastore_local_client_data"
private const val CLIENT_INTERCEPTOR = "client_interceptor"

val appModule = module {

    single(named(DATASTORE_LOCAL_CLIENT_DATA)) { androidContext().localClientDataPreferences }

    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        RegisterViewModel()
    }

    viewModel {
        UserAccountsListViewModel(get())
    }

    viewModel {
        AccountCardViewModel(get(), get())
    }


    single {
        AccountRepository(get())
    }
    single {
        UserRepository(get())
    }

    single {
        LoginUseCase(get(), get(named(DATASTORE_LOCAL_CLIENT_DATA)))
    }


    single(named(CLIENT_INTERCEPTOR)) {
        HeaderClientInterceptor(get(named(DATASTORE_LOCAL_CLIENT_DATA)))
    }

    single {
        val url = Uri.parse("http://10.0.2.2:50051/")
        val interceptor: HeaderClientInterceptor = get(named(CLIENT_INTERCEPTOR))
        ManagedChannelBuilder.forAddress(url.host, url.port).usePlaintext()
            .intercept(interceptor).build()
    }

    single<AccountOperationServiceStub> {
        val channel: ManagedChannel = get()
        AccountOperationServiceGrpc.newStub(channel)
    }

    single<UserOperationServiceStub> {
        val channel: ManagedChannel = get()
        UserOperationServiceGrpc.newStub(channel)
    }
}
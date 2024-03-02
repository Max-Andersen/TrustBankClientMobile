package com.trustbank.client_mobile.di

import android.net.Uri
import com.trustbank.client_mobile.domain.AccountRepository
import com.trustbank.client_mobile.presentation.authorization.login.LoginViewModel
import com.trustbank.client_mobile.presentation.authorization.register.RegisterViewModel
import com.trustbank.client_mobile.presentation.main.info.UserInfoViewModel
import com.trustbank.client_mobile.proto.AccountOperationsServiceGrpc
import com.trustbank.client_mobile.proto.AccountOperationsServiceGrpc.AccountOperationsServiceStub
import io.grpc.ManagedChannelBuilder
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module



val appModule = module {

    viewModel {
        LoginViewModel()
    }

    viewModel {
        RegisterViewModel()
    }

    viewModel{
        UserInfoViewModel(get())
    }


    single<AccountOperationsServiceStub> {
        val url = Uri.parse("http://10.0.2.2:50051/")
        val managedChannel = ManagedChannelBuilder.forAddress(url.host, url.port).usePlaintext().build()
        AccountOperationsServiceGrpc.newStub(managedChannel)
    }

    single {
        AccountRepository(get())
    }

}
package com.trustbank.client_mobile.di

import com.trustbank.client_mobile.presentation.authorization.login.LoginViewModel
import com.trustbank.client_mobile.presentation.authorization.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val appModule = module {

    viewModel {
        LoginViewModel()
    }

    viewModel {
        RegisterViewModel()
    }

}
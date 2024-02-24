package com.trustbank.client_mobile.presentation.authorization.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.presentation.authorization.login.LoginIntent.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    private val _effects = MutableSharedFlow<LoginEffect>()

    val uiState = _uiState.asStateFlow()
    val effects = _effects.asSharedFlow()

    private fun login(
        login: String,
        password: String
    ) {

    }

    fun reduce(intent: LoginIntent) {
        viewModelScope.launch {
            when (intent) {

                is LoginChanged -> {
                    _uiState.update {
                        it.copy(login = intent.newLogin)
                    }
                }

                is PasswordChanged -> {
                    _uiState.update {
                        it.copy(password = intent.newPassword)
                    }
                }

                is PasswordVisibilityChanged -> {
                    _uiState.update {
                        it.copy(isPasswordVisible = !it.isPasswordVisible)
                    }
                }

                is LoginClicked -> {
                    login(uiState.value.login, uiState.value.password)
                }

                is OnRegisterClicked -> {
                    _effects.emit(LoginEffect.OnRegisterClicked)
                }
            }
        }

    }

}

data class LoginUiState(
    val login: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false
)


sealed class LoginIntent {

    data class LoginChanged(val newLogin: String) : LoginIntent()

    data class PasswordChanged(val newPassword: String) : LoginIntent()

    data object PasswordVisibilityChanged : LoginIntent()

    data object LoginClicked : LoginIntent()

    data object OnRegisterClicked : LoginIntent()
}

sealed class LoginEffect {
    data object OnRegisterClicked : LoginEffect()
}
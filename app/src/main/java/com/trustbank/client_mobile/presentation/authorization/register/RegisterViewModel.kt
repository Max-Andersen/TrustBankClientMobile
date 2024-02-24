package com.trustbank.client_mobile.presentation.authorization.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trustbank.client_mobile.presentation.authorization.register.RegisterIntent.LoginChanged
import com.trustbank.client_mobile.presentation.authorization.register.RegisterIntent.LoginClicked
import com.trustbank.client_mobile.presentation.authorization.register.RegisterIntent.OnLoginClicked
import com.trustbank.client_mobile.presentation.authorization.register.RegisterIntent.PasswordChanged
import com.trustbank.client_mobile.presentation.authorization.register.RegisterIntent.PasswordConfirmationChanged
import com.trustbank.client_mobile.presentation.authorization.register.RegisterIntent.PasswordVisibilityChanged
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    private val _effects = MutableSharedFlow<RegisterEffect>()

    val uiState = _uiState.asStateFlow()
    val effects = _effects.asSharedFlow()

    private fun login(
        login: String,
        password: String,
        passwordConfirmation: String
    ) {

    }

    fun reduce(intent: RegisterIntent) {
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

                is PasswordConfirmationChanged -> {
                    _uiState.update {
                        it.copy(passwordConfirmation = intent.newPasswordConfirmation)
                    }
                }

                is PasswordVisibilityChanged -> {
                    _uiState.update {
                        it.copy(isPasswordVisible = !it.isPasswordVisible)
                    }
                }

                is LoginClicked -> {
                    login(
                        uiState.value.login,
                        uiState.value.password,
                        uiState.value.passwordConfirmation
                    )
                }

                is OnLoginClicked -> {
                    _effects.emit(RegisterEffect.OnLoginClicked)
                }
            }
        }

    }

}

data class RegisterUiState(
    val login: String = "",
    val password: String = "",
    val passwordConfirmation: String = "",
    val isPasswordVisible: Boolean = false
)


sealed class RegisterIntent {

    data class LoginChanged(val newLogin: String) : RegisterIntent()

    data class PasswordChanged(val newPassword: String) : RegisterIntent()

    data class PasswordConfirmationChanged(val newPasswordConfirmation: String) : RegisterIntent()

    data object PasswordVisibilityChanged : RegisterIntent()

    data object LoginClicked : RegisterIntent()

    data object OnLoginClicked : RegisterIntent()
}

sealed class RegisterEffect {
    data object OnLoginClicked : RegisterEffect()
}
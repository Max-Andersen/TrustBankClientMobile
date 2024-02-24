package com.trustbank.client_mobile.presentation.authorization.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.trustbank.client_mobile.R
import com.trustbank.client_mobile.presentation.navigation.AppNavigation
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreen(
    navController: NavController
) {

    val viewModel: RegisterViewModel = koinViewModel()

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is RegisterEffect.OnLoginClicked -> {
                    navController.navigate(AppNavigation.Login.routeTo())
                }
            }
        }
    }


    LoginScreenStateless(
        state,
        viewModel::reduce
    )
}

@Composable
private fun LoginScreenStateless(
    state: RegisterUiState,
    reduce: (RegisterIntent) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = { reduce(RegisterIntent.LoginClicked) }) {
                    Text(text = "Зарегистрироваться")
                }
                Button(onClick = { reduce(RegisterIntent.OnLoginClicked) }) {
                    Text(text = "У меня уже есть аккаунт")
                }
            }

        }
    ) { paddings ->

        Text(
            text = "TrustBank",
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddings.calculateTopPadding() + 50.dp),
            textAlign = TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = FontWeight.W600
        )



        Column(
            modifier = Modifier
                .fillMaxSize()
//                .padding(
//                    top = paddings.calculateTopPadding() + 16.dp,
//                    bottom = paddings.calculateBottomPadding() + 16.dp
//                )
            ,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = state.login,
                onValueChange = { newValue -> reduce(RegisterIntent.LoginChanged(newValue)) },
                placeholder = {
                    Text(text = "Придумайте логин")
                },
                trailingIcon = {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Default.Person, contentDescription = null
                    )
                }
            )

            TextField(
                value = state.password,
                onValueChange = { newValue -> reduce(RegisterIntent.PasswordChanged(newValue)) },
                placeholder = {
                    Text(text = "Придумайте пароль")
                },
                visualTransformation = if (state.isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = {
                        reduce(RegisterIntent.PasswordVisibilityChanged)
                    }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(id = if (state.isPasswordVisible) R.drawable.close_eye else R.drawable.open_eye),
                            contentDescription = null
                        )
                    }
                }
            )
            TextField(
                value = state.password,
                onValueChange = { newValue -> reduce(RegisterIntent.PasswordChanged(newValue)) },
                placeholder = {
                    Text(text = "Подтверждение пароля")
                },
                visualTransformation = PasswordVisualTransformation(),

                )
        }
    }
}

@Composable
@Preview
private fun LoginScreenPreview() {
    TrustBankClientMobileTheme {
        LoginScreenStateless(
            state = RegisterUiState(),
            reduce = {}
        )

    }
}
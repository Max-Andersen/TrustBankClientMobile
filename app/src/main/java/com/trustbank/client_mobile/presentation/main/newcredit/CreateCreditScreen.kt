package com.trustbank.client_mobile.presentation.main.newcredit

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.trustbank.client_mobile.presentation.ui.utils.OutlinedDoubleField
import com.trustbank.client_mobile.presentation.ui.utils.SpacerBig
import org.koin.androidx.compose.koinViewModel

@Composable
fun CreateCreditScreen(
    interestRatio: String,
    mainScreenNavigate: () -> Unit
) {
    val viewModel: CreateCreditViewModel = koinViewModel()

    LaunchedEffect(key1 = Unit) {
        viewModel.effect.collect {
            when (it) {
                is CreateCreditEffect.Success -> mainScreenNavigate()
                is CreateCreditEffect.Fail -> Log.d("CreateCreditScreen", "Fail create loan")
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center)
            ) {
                Spacer(modifier = Modifier.weight(0.5f))
                Text(text = "Ставка = ${interestRatio}%/день")
                SpacerBig()
                OutlinedDoubleField(
                    label = "Сумма кредита",
                    value = viewModel.issuedAmount.doubleValue,
                    onValueChange = { it?.let { viewModel.setIssuedAmount(it) } }
                )
                SpacerBig()
                OutlinedDoubleField(
                    label = "Количество дней",
                    value = viewModel.loanTermInDays.intValue.toDouble(),
                    onValueChange = { it?.let { viewModel.setDays(it.toInt()) } }
                )
                Spacer(modifier = Modifier.weight(0.5f))
                Button(
                    onClick = { viewModel.createCredit() },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Оформить")
                }
            }
        }
    }
}


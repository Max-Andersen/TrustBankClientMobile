package com.trustbank.client_mobile.presentation.main.loancreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.trustbank.client_mobile.presentation.ui.utils.SpacerBig
import com.trustbank.client_mobile.presentation.ui.utils.SpacerMedium
import com.trustbank.client_mobile.proto.LoanRequest

@Composable
fun LoanRequestListItem(
    item: LoanRequest,
    onClick: () -> Unit = {}
) {
    OutlinedCard(onClick = onClick) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "id = ${item.id}" ,
                style = MaterialTheme.typography.bodySmall,
            )
            SpacerMedium()
            Text(text = item.state.name)
            SpacerMedium()
            Text(text = "количество дней = ${item.loanTermInDays}")
            SpacerMedium()
            Text(text = "сумма = ${item.issuedAmount / 100.0} руб.")
            SpacerMedium()
            Text(text = "ставка ${item.tariff.interestRate}%/день")
        }
    }
}
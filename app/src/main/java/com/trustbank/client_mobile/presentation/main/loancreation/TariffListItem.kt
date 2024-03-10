package com.trustbank.client_mobile.presentation.main.loancreation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.protobuf.Timestamp
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
import com.trustbank.client_mobile.proto.LoanTariff

@Composable
fun TariffListItem(item: LoanTariff, onClick: () -> Unit = {}) {
    OutlinedCard(onClick = onClick) {
        Column(
            Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleLarge,
                maxLines = 3,
                minLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier.align(Alignment.End),
                shape = RoundedCornerShape(100),
                color = MaterialTheme.colorScheme.surfaceVariant
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 10.dp),
                    text = "${item.interestRate}%"
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TrustBankClientMobileTheme {
        Surface {
            TariffListItem(
                item = LoanTariff.newBuilder()
                    .setId("1")
                    .setName("Обычный тариф")
                    .setDescription("Описание")
                    .setInterestRate(15.5)
                    .setAdditionDate(Timestamp.getDefaultInstance())
                    .build()
            )
        }
    }
}
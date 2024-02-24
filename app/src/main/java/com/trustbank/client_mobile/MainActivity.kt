package com.trustbank.client_mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.trustbank.client_mobile.presentation.navigation.NavGraph
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrustBankClientMobileTheme {
                NavGraph(rememberNavController())
            }
        }
    }
}

package com.trustbank.client_mobile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.trustbank.client_mobile.presentation.navigation.NavGraph
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
import io.grpc.ManagedChannelBuilder
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver

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

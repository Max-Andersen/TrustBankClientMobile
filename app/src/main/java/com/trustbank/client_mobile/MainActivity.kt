package com.trustbank.client_mobile

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.trustbank.client_mobile.presentation.navigation.NavGraph
import com.trustbank.client_mobile.presentation.ui.theme.TrustBankClientMobileTheme
import com.trustbank.client_mobile.proto.AccountOperations
import com.trustbank.client_mobile.proto.AccountOperationsServiceGrpc
import com.trustbank.client_mobile.proto.HelloRequest
import com.trustbank.client_mobile.proto.HelloResponse
import io.grpc.ManagedChannelBuilder
import io.grpc.okhttp.OkHttpChannelBuilder
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TrustBankClientMobileTheme {
                NavGraph(rememberNavController())

                val a = object: ClientResponseObserver<HelloRequest, HelloResponse> {
                    override fun beforeStart(requestStream: ClientCallStreamObserver<HelloRequest>?) {
                        // save respObs for later
                    }
                    override fun onNext(value: HelloResponse) {
                        Log.d("GRPC", value.message)
                    }
                    override fun onError(t: Throwable?) {
                        Log.d("GRPC", t?.message ?: "Unknow error")
                    }
                    override fun onCompleted() {
                        Log.d("GRPC", "Completed")
                    }
                }

                val url = Uri.parse("http://10.0.2.2:50051/")
                var managedChannel = ManagedChannelBuilder.forAddress(url.host, url.port).usePlaintext().build()
                var blockingStub = AccountOperationsServiceGrpc.newStub(managedChannel)

                var bookRequest = HelloRequest.newBuilder().setName("world!!!!!").build() //AccountOperations.HelloRe.newBuilder().setIsbn(9123471293487).build()
                var book = blockingStub.helloWorld (bookRequest, a)
//                Log.d("GRPC", "$book")


            }
        }
    }
}

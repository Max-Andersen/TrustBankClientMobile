package com.trustbank.client_mobile.data

import android.provider.ContactsContract.Data
import androidx.datastore.core.DataStore
import com.trustbank.client_mobile.proto.LocalClientPreferences
import io.grpc.CallOptions
import io.grpc.Channel
import io.grpc.ClientCall
import io.grpc.ClientInterceptor
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall
import io.grpc.MethodDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class HeaderClientInterceptor(
    private val userDataStore: DataStore<LocalClientPreferences>
) : ClientInterceptor {

    val CUSTOM_HEADER_KEY: io.grpc.Metadata.Key<String> =
        io.grpc.Metadata.Key.of(
            "client_id_header",
            io.grpc.Metadata.ASCII_STRING_MARSHALLER
        )

    override fun <ReqT, RespT> interceptCall(
        method: MethodDescriptor<ReqT, RespT>?,
        callOptions: CallOptions?, next: Channel
    ): ClientCall<ReqT, RespT> {
        return object : SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            override fun start(responseListener: Listener<RespT>?, headers: io.grpc.Metadata) {
                val clientId = runBlocking { userDataStore.data.first().id }
                headers.put(CUSTOM_HEADER_KEY, clientId)
                super.start(responseListener, headers)
            }
        }
    }
//
//    companion object {
//        private val logger: Logger = Logger.getLogger(HeaderClientInterceptor::class.java.getName())
//
//        @VisibleForTesting
//        val CUSTOM_HEADER_KEY: Metadata.Key<String> =
//            Metadata.Key.of("custom_client_header_key", Metadata.ASCII_STRING_MARSHALLER)
//    }
}
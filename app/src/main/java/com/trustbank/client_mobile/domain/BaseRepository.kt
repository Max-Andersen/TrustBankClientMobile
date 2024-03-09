package com.trustbank.client_mobile.domain

import android.util.Log
import io.grpc.stub.ClientCallStreamObserver
import io.grpc.stub.ClientResponseObserver
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.runBlocking

open class BaseRepository {
    fun <T, U> getObserver(
        resultFlow: MutableSharedFlow<Result<U>>,
        onNext: ((U) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
        onCompleted: () -> Unit = {}
    ): ClientResponseObserver<T, U> {
        return object : ClientResponseObserver<T, U> {
            override fun beforeStart(requestStream: ClientCallStreamObserver<T>?) {}

            override fun onNext(value: U) {
                Log.d("Grpc", "got data $value")
                onNext?.let {
                    it(value)
                } ?: runBlocking {
                    resultFlow.emit(Result.success(value))
                }
            }

            override fun onError(t: Throwable?) {
                Log.d("Grpc", "request failed with error $t")
                onError?.let {
                    it(t!!)
                } ?: runBlocking {
                    resultFlow.emit(Result.failure(t!!))
                }
            }

            override fun onCompleted() {
                Log.d("Grpc", "request completed")
                onCompleted()
            }
        }
    }
}
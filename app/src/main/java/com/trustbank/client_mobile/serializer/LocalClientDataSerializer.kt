package com.trustbank.client_mobile.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import com.trustbank.client_mobile.proto.LocalClientPreferences
import java.io.InputStream
import java.io.OutputStream

class LocalClientDataSerializer : Serializer<LocalClientPreferences> {
    override val defaultValue: LocalClientPreferences =
        LocalClientPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): LocalClientPreferences {
        try {
            return LocalClientPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: LocalClientPreferences, output: OutputStream) =
        t.writeTo(output)
}
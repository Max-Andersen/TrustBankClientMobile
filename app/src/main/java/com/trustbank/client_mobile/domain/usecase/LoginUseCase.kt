package com.trustbank.client_mobile.domain.usecase

import androidx.datastore.core.DataStore
import com.trustbank.client_mobile.domain.UserRepository
import com.trustbank.client_mobile.proto.LocalClientPreferences
import kotlinx.coroutines.flow.onEach

class LoginUseCase(
    private val userRepository: UserRepository,
    private val clientDataStore: DataStore<LocalClientPreferences>
) {
    suspend operator fun invoke(login: String, password: String) =
        userRepository.login(login, password).onEach { response ->
            response.onSuccess { client ->
                clientDataStore.updateData { it.toBuilder().setId(client.id).build() }
            }
        }

}
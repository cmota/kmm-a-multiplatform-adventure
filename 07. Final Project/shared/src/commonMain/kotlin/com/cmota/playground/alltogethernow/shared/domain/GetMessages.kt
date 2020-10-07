package com.cmota.playground.alltogethernow.shared.domain

import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.cmota.playground.alltogethernow.shared.PlatformSettings.settingsRepository
import com.cmota.playground.alltogethernow.shared.data.entities.Message
import com.cmota.playground.alltogethernow.shared.subscribeCollectionSnapshot

import kotlinx.coroutines.coroutineScope

private const val TAG = "GetMessages"

class GetMessages {

    suspend operator fun invoke(onSuccess: (List<Message>) -> Unit) {
        coroutineScope {
            Gutenberg.d(TAG, "invoke")

            val subscription: (List<Message>) -> Unit = {
                Gutenberg.d(TAG, "Retrieved ${it.size} messages")

                for (message in it) {
                    message.outgoing = message.user == (settingsRepository.getUsername())
                }

                onSuccess(it)
            }

            subscribeCollectionSnapshot(subscription)
        }
    }
}
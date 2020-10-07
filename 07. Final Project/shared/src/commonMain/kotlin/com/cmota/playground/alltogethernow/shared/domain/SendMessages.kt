package com.cmota.playground.alltogethernow.shared.domain

import com.cmota.playground.alltogethernow.shared.sendMessage

import kotlinx.coroutines.coroutineScope

class SendMessages {

    suspend operator fun invoke(message: String, onResult: (Boolean) -> Unit) {
        coroutineScope {
            sendMessage(message, onResult)
        }
    }
}
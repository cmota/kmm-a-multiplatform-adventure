package com.cmota.playground.alltogethernow.shared.presentation.cb

import com.cmota.playground.alltogethernow.shared.data.entities.Message

interface IMessageData {

    fun onMessagesUpdate(messages: List<Message>)

    fun onMessageSent(status: Boolean)
}
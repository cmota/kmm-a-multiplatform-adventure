package com.cmota.playground.alltogethernow.shared.presentation

import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.cmota.playground.alltogethernow.shared.domain.GetMessages
import com.cmota.playground.alltogethernow.shared.domain.SendMessages
import com.cmota.playground.alltogethernow.shared.domain.defaultDispatcher
import com.cmota.playground.alltogethernow.shared.presentation.cb.IMessageData
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

private const val TAG = "MessageListPresenter"

class MessageListPresenter(private val getMessages: GetMessages,
                           private val sendMessages: SendMessages,
                           private val coroutineContext: CoroutineContext = defaultDispatcher
) {

    private var view: IMessageData? = null
    private val scope by lazy {
        PresenterCoroutineScope(coroutineContext)
    }

    fun subscribe(currView: IMessageData) {
        view = currView
        subscribe()
    }

    fun sendMessage(message: String) {
        scope.launch {
            sendMessages(
                message,
                onResult = { view?.onMessageSent(it) }
            )
        }
    }

    fun detachView() {
        scope.viewDetached()

        if (view == null) {
            return
        }

        view = null
    }

    private fun subscribe() {
        scope.launch {
            getMessages(
                onSuccess = { view?.onMessagesUpdate(it) }
            )
        }
    }
}
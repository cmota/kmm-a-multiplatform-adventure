package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.entities.Message
import com.cmota.playground.alltogethernow.shared.presentation.cb.IMessageData

expect class FirebaseFirestore

expect fun getFirebaseInstance(): FirebaseFirestore

expect fun subscribeCollectionSnapshot(onSuccess: (List<Message>) -> Unit)

expect fun sendMessage(message: String, onResult: (Boolean) -> Unit)
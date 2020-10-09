package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.entities.Message
import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.google.firebase.firestore.FirebaseFirestore.getInstance

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

actual typealias FirebaseFirestore = com.google.firebase.firestore.FirebaseFirestore

actual fun getFirebaseInstance() = getInstance()

const val TAG = "PlatformFirebaseFirestore-Android"

actual fun sendMessage(message: String, onResult: (Boolean) -> Unit) {

   val now: Instant = Clock.System.now()
   val millis = now.toEpochMilliseconds()

   val username = PlatformSettings.settingsRepository.getUsername()

   val content = hashMapOf(
               "username" to username,
               "content" to message,
               "timestamp" to "$millis"
           )

   val id: String = getFirebaseInstance().collection("collection_name").document().id
   getFirebaseInstance().collection(COLLECTION_DCEMEA).document(id)
       .set(content)
       .addOnSuccessListener { onResult(true) }
       .addOnFailureListener { onResult(false) }
}
package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.entities.Message
import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.google.firebase.firestore.FirebaseFirestore.getInstance

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


actual typealias FirebaseFirestore = com.google.firebase.firestore.FirebaseFirestore

actual fun getFirebaseInstance() = getInstance()

const val TAG = "PlatformFirebaseFirestore-Android"

actual fun subscribeCollectionSnapshot(onSuccess: (List<Message>) -> Unit) {
    val docs = getFirebaseInstance().collection(COLLECTION_DCEMEA).orderBy("timestamp")
    docs.addSnapshotListener { snapshot, e ->
        if (e != null || snapshot == null) {
            Gutenberg.w(TAG, "Unable to retrieve data. Error=$e, snapshot=$snapshot")
            return@addSnapshotListener
        }

        Gutenberg.d(TAG, "New data retrieved:${snapshot.documents.size}")

        val messages = mutableListOf<Message>()
        for (document in snapshot.documents) {
            val message = Message(
                document.id,
                "${document.data?.get("username")}",
                "${document.data?.get("content")}",
                "${document.data?.get("timestamp")}"
            )

            messages += message
        }

        onSuccess(messages)
    }
}

actual fun sendMessage(message: String, onResult: (Boolean) -> Unit) {
    val now: Instant = Clock.System.now()
    now.toEpochMilliseconds()

    val username = PlatformSettings.settingsRepository.getUsername()

    val content = hashMapOf(
                "username" to username,
                "content" to message,
                "timestamp" to "$now"
            )

    val id: String = getFirebaseInstance().collection("collection_name").document().id
    getFirebaseInstance().collection(COLLECTION_DCEMEA).document(id)
        .set(content)
        .addOnSuccessListener { onResult(true) }
        .addOnFailureListener { onResult(false) }
}
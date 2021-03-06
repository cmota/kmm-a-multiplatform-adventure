package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.entities.Message
import cocoapods.FirebaseFirestore.FIRFirestore
import cocoapods.FirebaseFirestore.FIRCollectionReference
import cocoapods.FirebaseFirestore.FIRQuery
import cocoapods.FirebaseFirestore.FIRDocumentReference

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

actual typealias FirebaseFirestore = FIRFirestore

actual fun getFirebaseInstance() = FIRFirestore.firestore()

class CollectionReference(override val ios: FIRCollectionReference) : Query(ios) {

   val path: String
       get() = ios.path

   fun add(data: Any) = DocumentReference(ios.addDocumentWithData(data as Map<Any?, *>, null))
}

class DocumentReference(val ios: FIRDocumentReference) {

   val id: String
       get() = ios.documentID

   val path: String
       get() = ios.path
}

open class Query(open val ios: FIRQuery) {
   // Nothing is required for the calls defined for this project
}

fun collection(firestore: FirebaseFirestore, collectionPath: String) = CollectionReference(firestore.collectionWithPath(collectionPath))

actual fun sendMessage(message: String, onResult: (Boolean) -> Unit) {
   //6
   val now: Instant = Clock.System.now()
   now.toEpochMilliseconds()

   //7
   val username = PlatformSettings.settingsRepository.getUsername()

   val content = hashMapOf(
               "username" to username,
               "content" to message,
               "timestamp" to "$now"
           )

   collection(getFirebaseInstance(), COLLECTION_DCEMEA).add(content)
}
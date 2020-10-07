package com.cmota.playground.alltogethernow.shared.domain

import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.cmota.playground.alltogethernow.shared.data.ConferencesAPI
import com.cmota.playground.alltogethernow.shared.data.entities.Conference
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val TAG = "GetConferences"

class GetConferences(private val api: ConferencesAPI) {

    suspend operator fun invoke(onSuccess: (List<Conference>) -> Unit, onFailure: (Exception) -> Unit) {
        try {

            val result = api.fetchConferences()

            val conferences = Json.decodeFromString<List<Conference>>(result)

            Gutenberg.d(TAG, "Result:$conferences")

            coroutineScope {
                onSuccess(conferences)
            }
        } catch (e: Exception) {
            coroutineScope {
                onFailure(e)
            }
        }
    }
}
package com.cmota.playground.alltogethernow.shared.domain

import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.cmota.playground.alltogethernow.shared.data.ConferencesAPI
import com.cmota.playground.alltogethernow.shared.data.entities.Conference
import com.cmota.playground.alltogethernow.shared.domain.dao.ConferenceDAO
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val TAG = "GetConferences"

class GetConferences(private val api: ConferencesAPI, private val dao: ConferenceDAO) {

    suspend operator fun invoke(onSuccess: (List<Conference>) -> Unit, onFailure: (Exception) -> Unit) {
        try {

            val result = api.fetchConferences()

            val conferences = Json.decodeFromString<List<Conference>>(result)

            Gutenberg.d(TAG, "Result:$conferences")

            for (conference in conferences) {
                dao.insertOrReplace(conference)
            }

            coroutineScope {
                onSuccess(conferences)
            }
        } catch (e: Exception) {
            coroutineScope {

                val conferences = dao.getAllConferences()
                if (conferences.isEmpty()) {
                    onFailure(e)
                } else {
                    onSuccess(conferences)
                }
            }
        }
    }
}
package com.cmota.playground.alltogethernow.shared.data

import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

private const val BASE_URL = "https://gist.githubusercontent.com/cmota/"
private const val ENDPOINT = "c6b15f54c9fed96750e5828b2f001249/raw/d7fc5e1b711107583959663056e6643f24ccae81/conferences.json"

class ConferencesAPI {

    private val client = HttpClient()
    /**
     * If the response was an application/json we would need to install [JsonFeature] on [HttpClient]
     * something similar to:
     *
     * private val jsonParser = Json { ignoreUnknownKeys = true; isLenient = true }
     *
     * private val client = HttpClient() {
     *   install(JsonFeature) {
     *     serializer = KotlinxSerializer(jsonParser)
     *   }
     * }
     *
     *
     * However, on the latest version of Ktor (1.4.1) there's an issue that would trigger an
     * InvalidMutabilityException` when running on iOS. If on your use case you want to parse a
     * json file automatically, I advise you to rollback to ktor 1.4.0
     */

    /** This is a request to GitHub Gists that returns application/json as text/plain which is not
     *  not currently supported. If it was, instead of `String` it should be `List<Conference>`.
     *
     *  With this, we'll get the `List<Conference>` by decoding the response on
     *  [com.cmota.playground.alltogethernow.shared.domain.GetConferences]
     */
    suspend fun fetchConferences() = client.get<String>("$BASE_URL$ENDPOINT")
}
package com.cmota.playground.alltogethernow.shared.data

import io.ktor.client.HttpClient
import io.ktor.client.request.*

private const val BASE_URL = "https://gist.githubusercontent.com/cmota/"
private const val ENDPOINT = "c6b15f54c9fed96750e5828b2f001249/raw/d7fc5e1b711107583959663056e6643f24ccae81/conferences.json"

class ConferencesAPI {

    private val client = HttpClient()

    suspend fun fetchConferences() = client.get<String>("$BASE_URL$ENDPOINT")
}
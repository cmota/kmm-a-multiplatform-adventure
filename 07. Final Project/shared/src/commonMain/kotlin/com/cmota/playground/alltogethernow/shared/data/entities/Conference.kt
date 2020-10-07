package com.cmota.playground.alltogethernow.shared.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class Conference (val name: String,
                       val city: String,
                       val country: String,
                       val date: String,
                       val logo: String,
                       val website: String,
                       val status: String) {

    fun isCanceled() = status == "canceled"
}
package com.cmota.playground.alltogethernow.shared.data.entities

data class Message(val id: String,
                   val user: String,
                   val content: String,
                   val timestamp: String,
                   var outgoing: Boolean = false)
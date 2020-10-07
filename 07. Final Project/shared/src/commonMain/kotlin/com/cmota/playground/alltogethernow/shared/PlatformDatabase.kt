package com.cmota.playground.alltogethernow.shared

import data.ConferenceDb

expect class PlatformDatabase {

    fun createDatabase(): ConferenceDb
}
package com.cmota.playground.alltogethernow.shared

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import data.ConferenceDb

actual class PlatformDatabase {

    actual fun createDatabase(): ConferenceDb {
        return ConferenceDb(createDriver())
    }

    private fun createDriver(): SqlDriver {
        return NativeSqliteDriver(ConferenceDb.Schema, "appData.db")
    }
}
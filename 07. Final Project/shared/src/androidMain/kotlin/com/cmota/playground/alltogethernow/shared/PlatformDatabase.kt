package com.cmota.playground.alltogethernow.shared

import android.content.Context
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import data.ConferenceDb

lateinit var appContext: Context

actual class PlatformDatabase {

    actual fun createDatabase(): ConferenceDb {
        return ConferenceDb(createDriver())
    }

    private fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(ConferenceDb.Schema, appContext, "appData.db")
    }
}
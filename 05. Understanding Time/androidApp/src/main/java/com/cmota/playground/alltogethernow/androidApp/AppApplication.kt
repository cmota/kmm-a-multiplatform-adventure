package com.cmota.playground.alltogethernow.androidApp

import android.app.Application
import com.cmota.playground.alltogethernow.shared.appContext

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        appContext = this
    }
}
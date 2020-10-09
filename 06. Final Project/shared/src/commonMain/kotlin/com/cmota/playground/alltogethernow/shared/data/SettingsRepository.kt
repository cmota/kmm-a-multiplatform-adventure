package com.cmota.playground.alltogethernow.shared.data

import com.cmota.playground.alltogethernow.shared.deviceName

import com.russhwolf.settings.Settings

private const val SETTING_ONLY_ONLINE = "setting_show_only_online"
private const val SETTING_MY_USERNAME = "setting_my_username"

class SettingsRepository(private val settings: Settings) {

    private val appSettings: Settings = createAppSettings(settings)

    private fun createAppSettings(settings: Settings): Settings {
        settings.putString(SETTING_MY_USERNAME, deviceName())
        settings.putBoolean(SETTING_ONLY_ONLINE, false)
        return settings
    }

    fun getUsername() = appSettings.getString(SETTING_MY_USERNAME, deviceName())

    fun setUsername(username: String) {
        appSettings.putString(SETTING_MY_USERNAME, username)
    }

    fun shouldShowOnlyOnlineConferences() = appSettings.getBoolean(SETTING_ONLY_ONLINE, false)

    fun onlyOnlineConferences(state: Boolean) {
        appSettings.putBoolean(SETTING_ONLY_ONLINE, state)
    }
}
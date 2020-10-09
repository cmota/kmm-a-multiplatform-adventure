package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.SettingsRepository

expect object PlatformSettings {

    val settingsRepository: SettingsRepository

    fun createSettingsRepository(): SettingsRepository
}
package com.cmota.playground.alltogethernow.shared

internal expect class PlatformLogger {

    fun debug(tag: String, message: String)

    fun warn(tag: String, message: String)

    fun error(tag: String, message: String)
}

object Gutenberg {

    private val logger = PlatformLogger()

    fun d(tag: String, message: String) {
        logger.debug(tag, message)
    }

    fun w(tag: String, message: String) {
        logger.warn(tag, message)
    }

    fun e(tag: String, message: String) {
        logger.error(tag, message)
    }
}
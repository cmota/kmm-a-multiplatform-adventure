package com.cmota.playground.alltogethernow.shared.domain

import kotlin.coroutines.CoroutineContext

actual val uiDispatcher: CoroutineContext
    get() = IosMainDispatcher

actual val defaultDispatcher: CoroutineContext
    get() = IosMainDispatcher
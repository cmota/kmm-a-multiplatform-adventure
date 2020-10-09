package com.cmota.playground.alltogethernow.shared.domain

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val uiDispatcher: CoroutineContext
    get() = Dispatchers.Main

actual val defaultDispatcher: CoroutineContext
    get() = Dispatchers.Default
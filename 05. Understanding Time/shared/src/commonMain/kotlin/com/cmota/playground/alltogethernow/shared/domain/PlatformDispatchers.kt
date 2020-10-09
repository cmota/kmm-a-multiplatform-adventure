package com.cmota.playground.alltogethernow.shared.domain

import kotlin.coroutines.CoroutineContext

expect val defaultDispatcher: CoroutineContext

expect val uiDispatcher: CoroutineContext
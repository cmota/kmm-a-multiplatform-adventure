package com.cmota.playground.alltogethernow.shared

import android.os.Build.MANUFACTURER
import android.os.Build.MODEL

actual fun deviceName() = "$MODEL-$MANUFACTURER"
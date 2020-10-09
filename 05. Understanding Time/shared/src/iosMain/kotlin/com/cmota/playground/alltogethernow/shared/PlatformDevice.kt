package com.cmota.playground.alltogethernow.shared

import platform.UIKit.UIDevice

actual fun deviceName() = "${UIDevice.currentDevice().name}-${UIDevice.currentDevice().model}"

package com.enricog.features.timer.util

import java.util.Timer

internal interface TimerProvider {
    fun get() : Timer
}

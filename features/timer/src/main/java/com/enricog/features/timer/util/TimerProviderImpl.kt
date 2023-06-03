package com.enricog.features.timer.util

import java.util.Timer
import javax.inject.Inject

internal class TimerProviderImpl @Inject constructor() : TimerProvider {
    override fun get(): Timer {
        return Timer()
    }
}

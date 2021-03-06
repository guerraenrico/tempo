package com.enricog.entities.routines

import com.enricog.entities.Seconds
import com.enricog.entities.seconds

enum class TimeType(val progress: Seconds) {
    STOPWATCH(progress = 1.seconds),
    TIMER(progress = (-1).seconds),
    REST(progress = (-1).seconds)
}

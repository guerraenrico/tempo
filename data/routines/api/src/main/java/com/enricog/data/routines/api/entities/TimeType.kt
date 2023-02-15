package com.enricog.data.routines.api.entities

import com.enricog.entities.Seconds
import com.enricog.entities.seconds

enum class TimeType(val progress: Seconds, val requirePreparationTime: Boolean) {
    STOPWATCH(progress = 1.seconds, requirePreparationTime = true),
    TIMER(progress = (-1).seconds, requirePreparationTime = true),
    REST(progress = (-1).seconds, requirePreparationTime = false)
}

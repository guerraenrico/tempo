package com.enricog.entities.routines

enum class TimeType(val progress: Long) {
    STOPWATCH(progress = 1),
    TIMER(progress = -1),
    REST(progress = -1)
}

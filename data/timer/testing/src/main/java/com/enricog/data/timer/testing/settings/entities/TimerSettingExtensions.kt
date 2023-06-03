package com.enricog.data.timer.testing.settings.entities

import com.enricog.data.timer.api.settings.entities.TimerSettings

val TimerSettings.Companion.DEFAULT: TimerSettings
    get() = TimerSettings(
        soundEnabled = true,
        runInBackgroundEnabled = false
    )

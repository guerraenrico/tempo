package com.enricog.data.timer.testing.entities

import com.enricog.core.entities.asID
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.api.theme.entities.TimerTheme.Asset.Color
import com.enricog.data.timer.api.theme.entities.TimerTheme.Resource
import java.time.OffsetDateTime

val TimerTheme.Companion.DEFAULT: TimerTheme
    get() = TimerTheme(
        id = 0.asID,
        name = "",
        description = "",
        preparationTimeResource = Resource(
            // Color.Blue
            background = Color(argb = "18374687574888284160".toULong()),
            // Color.White
            onBackground = Color(argb = "18446744069414584320".toULong())
        ),
        stopwatchResource = Resource(
            // Color.Black
            background = Color(argb = "18374686479671623680".toULong()),
            // Color.White
            onBackground = Color(argb = "18446744069414584320".toULong())
        ),
        timerResource = Resource(
            // Color.Red
            background = Color(argb = "18446462598732840960".toULong()),
            // Color.White
            onBackground = Color(argb = "18446744069414584320".toULong())
        ),
        restResource = Resource(
            // Color.Green
            background = Color(argb = "18374966855136706560".toULong()),
            // Color.White
            onBackground = Color(argb = "18446744069414584320".toULong())
        ),
        isLocked = false,
        isDefault = true,
        createdAt = OffsetDateTime.MIN,
        updatedAt = OffsetDateTime.MIN
    )
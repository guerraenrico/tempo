package com.enricog.features.routines.detail.ui.time_type

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.enricog.features.routines.R
import com.enricog.ui.theme.TimeTypeColors
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

internal data class TimeType(@StringRes val nameStringResId: Int, val color: Color) {

    companion object {
        fun from(timeType: TimeTypeEntity) = TimeType(
            nameStringResId = timeType.nameStringResId(),
            color = timeType.color()
        )
    }
}

internal fun TimeTypeEntity.color(): Color {
    return when (this) {
        TimeTypeEntity.TIMER -> TimeTypeColors.TIMER
        TimeTypeEntity.REST -> TimeTypeColors.REST
        TimeTypeEntity.STOPWATCH -> TimeTypeColors.STOPWATCH
    }
}

@StringRes
internal fun TimeTypeEntity.nameStringResId(): Int {
    return when (this) {
        TimeTypeEntity.REST -> R.string.chip_time_type_rest_name
        TimeTypeEntity.TIMER -> R.string.chip_time_type_timer_name
        TimeTypeEntity.STOPWATCH -> R.string.chip_time_type_stopwatch_name
    }
}

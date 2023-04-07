package com.enricog.features.routines.detail.ui.time_type

import androidx.annotation.StringRes
import androidx.compose.runtime.saveable.Saver
import androidx.compose.ui.graphics.Color
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.routines.R
import com.enricog.data.routines.api.entities.TimeType
import java.io.Serializable

internal data class TimeTypeStyle(
    val id: String,
    val backgroundColor: Color,
    val onBackgroundColor: Color,
    @StringRes val nameStringResId: Int
): Serializable {

    fun toEntity(): TimeType {
        return TimeType.valueOf(id)
    }

    companion object {

        fun Saver() = Saver<TimeTypeStyle, TimeTypeStyle>(
            save = { it },
            restore = { it }
        )

        fun from(timeType: TimeType, timerTheme: TimerTheme): TimeTypeStyle {
            val resource = when(timeType) {
                TimeType.STOPWATCH -> timerTheme.stopwatchResource
                TimeType.TIMER -> timerTheme.timerResource
                TimeType.REST -> timerTheme.restResource
            }
            return  TimeTypeStyle(
                id = timeType.name,
                backgroundColor = resource.background.toColor(),
                onBackgroundColor = resource.onBackground.toColor(),
                nameStringResId = timeType.nameStringResId()
            )
        }
    }
}

private fun TimerTheme.Asset.toColor(): Color {
    return when(this) {
        is TimerTheme.Asset.Color -> Color(argb)
    }
}

@StringRes
private fun TimeType.nameStringResId(): Int {
    return when (this) {
        TimeType.REST -> R.string.chip_time_type_rest_name
        TimeType.TIMER -> R.string.chip_time_type_timer_name
        TimeType.STOPWATCH -> R.string.chip_time_type_stopwatch_name
    }
}

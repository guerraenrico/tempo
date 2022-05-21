package com.enricog.features.routines.detail.ui.time_type

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.R
import com.enricog.ui.theme.TimeTypeColors

internal fun TimeType.color(): Color {
    return when (this) {
        TimeType.TIMER -> TimeTypeColors.TIMER
        TimeType.REST -> TimeTypeColors.REST
        TimeType.STOPWATCH -> TimeTypeColors.STOPWATCH
    }
}

@Composable
internal fun TimeType.text(): String {
    return when (this) {
        TimeType.REST -> stringResource(R.string.chip_time_type_rest_name)
        TimeType.TIMER -> stringResource(R.string.chip_time_type_timer_name)
        TimeType.STOPWATCH -> stringResource(R.string.chip_time_type_stopwatch_name)
    }
}

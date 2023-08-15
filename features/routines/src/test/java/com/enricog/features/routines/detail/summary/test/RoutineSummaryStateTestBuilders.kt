package com.enricog.features.routines.detail.summary.test

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState

internal fun RoutineSummaryStateData(block: RoutineSummaryStateBuilder.Data.() -> Unit): RoutineSummaryState.Data {
    return RoutineSummaryStateBuilder.Data().apply(block).build()
}

internal fun RoutineSummaryStateError(block: RoutineSummaryStateBuilder.Error.() -> Unit): RoutineSummaryState.Error {
    return RoutineSummaryStateBuilder.Error().apply(block).build()
}

internal class RoutineSummaryStateBuilder {

    class Data {

        var timerTheme: TimerTheme = TimerTheme.DEFAULT
        var routine: Routine = Routine.EMPTY
        var statistics: List<Statistic> = listOf(Statistic.EMPTY)
        var errors: Map<RoutineSummaryField, RoutineSummaryFieldError> = emptyMap()
        var action: RoutineSummaryState.Data.Action? = null

        fun build(): RoutineSummaryState.Data {
            return RoutineSummaryState.Data(
                routine = routine,
                errors = errors,
                statistics = statistics,
                action = action,
                timerTheme = timerTheme
            )
        }
    }

    class Error {

        var throwable: Throwable = Exception("Something went wrong")

        fun build(): RoutineSummaryState.Error {
            return RoutineSummaryState.Error(throwable = throwable)
        }
    }
}

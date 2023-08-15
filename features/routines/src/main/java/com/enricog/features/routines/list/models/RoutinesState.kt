package com.enricog.features.routines.list.models

import com.enricog.data.routines.api.entities.Routine
import com.enricog.core.entities.ID
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.timer.api.theme.entities.TimerTheme

internal sealed class RoutinesState {

    object Idle : RoutinesState()

    object Empty : RoutinesState()

    data class Data(
        val routines: List<Routine>,
        val timerTheme: TimerTheme,
        val statistics: List<Statistic>,
        val action: Action?
    ) : RoutinesState() {
        sealed class Action {
            data class DeleteRoutineError(val routineId: ID) : Action()
            object DeleteRoutineSuccess : Action()
            object MoveRoutineError : Action()
            object DuplicateRoutineError : Action()
        }
    }

    data class Error(val throwable: Throwable) : RoutinesState()
}

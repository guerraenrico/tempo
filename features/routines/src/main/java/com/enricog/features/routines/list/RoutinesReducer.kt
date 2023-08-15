package com.enricog.features.routines.list

import com.enricog.core.entities.ID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineSuccess
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DuplicateRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.MoveRoutineError
import javax.inject.Inject

internal class RoutinesReducer @Inject constructor() {

    fun setup(
        state: RoutinesState,
        routines: List<Routine>,
        timerTheme: TimerTheme,
        statistics: List<Statistic>
    ): RoutinesState {
        return when {
            routines.isEmpty() -> RoutinesState.Empty
            state is RoutinesState.Data -> state.copy(routines = routines)
            else -> RoutinesState.Data(
                routines = routines,
                timerTheme = timerTheme,
                statistics = statistics,
                action = null
            )
        }
    }

    fun error(throwable: Throwable): RoutinesState {
        return RoutinesState.Error(throwable = throwable)
    }

    fun deleteRoutineError(state: RoutinesState.Data, routineId: ID): RoutinesState.Data {
        return state.copy(action = DeleteRoutineError(routineId = routineId))
    }

    fun deleteRoutineSuccess(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = DeleteRoutineSuccess)
    }

    fun moveRoutineError(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = MoveRoutineError)
    }

    fun duplicateRoutineError(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = DuplicateRoutineError)
    }

    fun actionHandled(state: RoutinesState.Data): RoutinesState.Data {
        return state.copy(action = null)
    }
}

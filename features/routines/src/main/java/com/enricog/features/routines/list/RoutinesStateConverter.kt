package com.enricog.features.routines.list

import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.compose.api.classes.asImmutableList
import com.enricog.core.compose.api.classes.asImmutableMap
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.features.routines.R
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.features.routines.list.models.RoutinesItem.RoutineItem
import com.enricog.features.routines.list.models.RoutinesItem.Space
import com.enricog.features.routines.list.models.RoutinesState
import com.enricog.features.routines.list.models.RoutinesState.Data.Action
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DeleteRoutineSuccess
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.DuplicateRoutineError
import com.enricog.features.routines.list.models.RoutinesState.Data.Action.MoveRoutineError
import com.enricog.features.routines.list.models.RoutinesViewState
import com.enricog.features.routines.list.models.RoutinesViewState.Data
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import com.enricog.features.routines.ui_components.goal_label.toGoalLabel
import com.enricog.ui.components.textField.timeText
import java.time.Clock
import javax.inject.Inject

internal class RoutinesStateConverter @Inject constructor(
    private val clock: Clock
) : StateConverter<RoutinesState, RoutinesViewState> {

    override suspend fun convert(state: RoutinesState): RoutinesViewState {
        return when (state) {
            RoutinesState.Idle -> RoutinesViewState.Idle
            RoutinesState.Empty -> RoutinesViewState.Empty
            is RoutinesState.Data -> Data(
                routinesItems = buildList {
                    addAll(state.routines.map { routine ->
                        routine.toViewItem(
                            timerTheme = state.timerTheme,
                            statistics = state.statistics.filter { it.routineId == routine.id }
                        )
                    })
                    add(Space)
                }.asImmutableList(),
                message = state.action?.toMessage()
            )

            is RoutinesState.Error -> RoutinesViewState.Error(throwable = state.throwable)
        }
    }

    private fun Routine.toViewItem(timerTheme: TimerTheme, statistics: List<Statistic>): RoutineItem {
        val segmentsSummary = if (segments.isNotEmpty()) {
            RoutineItem.SegmentsSummary(
                estimatedTotalTime = expectedTotalTime.takeIf { it > 0.seconds }?.timeText,
                segmentTypesCount = segments.groupBy { it.type }
                    .map { (type, segments) ->
                        TimeTypeStyle.from(timeType = type, timerTheme = timerTheme) to segments.size
                    }
                    .toMap()
                    .asImmutableMap()
            )
        } else null

        val goalLabel = frequencyGoal?.toGoalLabel(clock = clock, statistics = statistics)

        return RoutineItem(
            id = id,
            name = name,
            rank = rank.toString(),
            segmentsSummary = segmentsSummary,
            goalLabel = goalLabel
        )
    }

    private fun Action.toMessage(): Message {
        return when (this) {
            is DeleteRoutineError -> Message(
                textResId = R.string.label_routines_delete_error,
                actionTextResId = R.string.action_text_routines_delete_error
            )

            DeleteRoutineSuccess -> Message(
                textResId = R.string.label_routines_delete_confirm,
                actionTextResId = R.string.action_text_routines_delete_undo
            )

            MoveRoutineError -> Message(
                textResId = R.string.label_routines_move_error,
                actionTextResId = null
            )

            DuplicateRoutineError -> Message(
                textResId = R.string.label_routines_duplicate_error,
                actionTextResId = null
            )
        }
    }
}

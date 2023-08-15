package com.enricog.features.routines.ui_components.goal_label

import androidx.annotation.StringRes
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.features.routines.R
import java.time.Clock

internal data class GoalLabel(
    @StringRes val stringResId: Int,
    val formatArgs: ImmutableList<Any>
)

internal fun FrequencyGoal.toGoalLabel(clock: Clock, statistics: List<Statistic>): GoalLabel {
    val (from, to) = period.timeRange(clock = clock)
    val numCompletedRoutines = statistics.count {
        it.type == Statistic.Type.ROUTINE_COMPLETED && it.createdAt >= from && it.createdAt <= to
    }
    return when (period) {
        FrequencyGoal.Period.DAY -> GoalLabel(
            stringResId = R.string.label_routine_goal_text_day,
            formatArgs = immutableListOf(numCompletedRoutines, times)
        )

        FrequencyGoal.Period.WEEK -> GoalLabel(
            stringResId = R.string.label_routine_goal_text_week,
            formatArgs = immutableListOf(numCompletedRoutines, times)
        )

        FrequencyGoal.Period.MONTH -> GoalLabel(
            stringResId = R.string.label_routine_goal_text_month,
            formatArgs = immutableListOf(numCompletedRoutines, times)
        )
    }
}

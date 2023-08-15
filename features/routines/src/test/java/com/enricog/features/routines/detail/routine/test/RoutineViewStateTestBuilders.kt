package com.enricog.features.routines.detail.routine.test

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.core.compose.api.classes.ImmutableMap
import com.enricog.core.compose.api.classes.Text
import com.enricog.core.compose.api.classes.asImmutableList
import com.enricog.core.compose.api.classes.emptyImmutableMap
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import com.enricog.features.routines.detail.routine.models.RoutineViewState
import com.enricog.ui.components.dropDown.TempoDropDownItem
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText

internal fun RoutineViewStateData(block: RoutineViewStateBuilder.Data.() -> Unit): RoutineViewState.Data {
    return RoutineViewStateBuilder.Data().apply(block).build()
}

internal class RoutineViewStateBuilder {

    class Data {

        var errors: ImmutableMap<RoutineField, RoutineFieldError> = emptyImmutableMap()
        var message: RoutineViewState.Data.Message? = null
        var frequencyGoalItems: ImmutableList<TempoDropDownItem> = frequencyGoalDropDownItems.values.toList()
            .asImmutableList()

        fun build(): RoutineViewState.Data {
            return RoutineViewState.Data(
                errors = errors,
                message = message,
                frequencyGoalItems = frequencyGoalItems
            )
        }
    }
}

internal fun RoutineInputs(block : RoutineInputsBuilder.() -> Unit): RoutineInputs {
    return RoutineInputsBuilder().apply(block).build()
}

internal class RoutineInputsBuilder {

    var name: TextFieldValue = "Routine Name".toTextFieldValue()
    var preparationTime: TimeText = "0".timeText
    var rounds: TextFieldValue = "1".toTextFieldValue()
    var frequencyGoal: RoutineInputs.FrequencyGoalInput = RoutineInputs.FrequencyGoalInput.None

    fun from(routine: Routine) {
        name = routine.name.toTextFieldValue()
        preparationTime = routine.preparationTime.timeText
        rounds = routine.rounds.toString().toTextFieldValue()
        frequencyGoal = routine.frequencyGoal?.let {
            RoutineInputs.FrequencyGoalInput.Value(
                frequencyGoalTimes = it.times.toString().toTextFieldValue(),
                frequencyGoalPeriod = frequencyGoalDropDownItems.getValue(it.period)
            )
        } ?: RoutineInputs.FrequencyGoalInput.None
    }

    fun build(): RoutineInputs {
        return RoutineInputs(
            name = name,
            preparationTime = preparationTime,
            rounds = rounds,
            frequencyGoal = frequencyGoal
        )
    }
}

internal val frequencyGoalDropDownItems = mapOf(
    FrequencyGoal.Period.DAY to TempoDropDownItem(
        id = "DAY",
        text = Text.Resource(resId = R.string.frequency_goal_period_day_name)
    ),
    FrequencyGoal.Period.WEEK to TempoDropDownItem(
        id = "WEEK",
        text = Text.Resource(resId = R.string.frequency_goal_period_week_name)
    ),
    FrequencyGoal.Period.MONTH to TempoDropDownItem(
        id = "MONTH",
        text = Text.Resource(resId = R.string.frequency_goal_period_month_name)
    )
)

package com.enricog.features.routines.detail.routine.models

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.ui.components.dropDown.TempoDropDownItem
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.TimeText

internal data class RoutineInputs(
    val name: TextFieldValue,
    val preparationTime: TimeText,
    val rounds: TextFieldValue,
    val frequencyGoal: FrequencyGoalInput
) {

    sealed class FrequencyGoalInput {
        object None : FrequencyGoalInput()

        data class Value(
            val frequencyGoalTimes: TextFieldValue,
            val frequencyGoalPeriod: TempoDropDownItem
        ) : FrequencyGoalInput()

        companion object {
            fun from(frequencyGoal: FrequencyGoal?): FrequencyGoalInput {
                return if (frequencyGoal == null) {
                    None
                } else {
                    Value(
                        frequencyGoalTimes = frequencyGoal.times.toString().toTextFieldValue(),
                        frequencyGoalPeriod = frequencyGoal.period.toDropDownItem()
                    )
                }
            }
        }
    }

    fun mergeToRoutine(routine: Routine): Routine {
        return routine.copy(
            name = name.text,
            preparationTime = preparationTime.toSeconds(),
            rounds = rounds.text.toInt(),
            frequencyGoal = when (frequencyGoal) {
                FrequencyGoalInput.None -> null
                is FrequencyGoalInput.Value -> FrequencyGoal(
                    times = frequencyGoal.frequencyGoalTimes.text.toInt(),
                    period = FrequencyGoal.Period.valueOf(frequencyGoal.frequencyGoalPeriod.id)
                )
            }
        )
    }

    companion object {
        val empty = RoutineInputs(
            name = "".toTextFieldValue(),
            preparationTime = TimeText.from(0.seconds),
            rounds = "1".toTextFieldValue(),
            frequencyGoal = FrequencyGoalInput.None
        )
    }
}
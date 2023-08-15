package com.enricog.features.routines.detail.routine

import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.detail.routine.models.RoutineField
import com.enricog.features.routines.detail.routine.models.RoutineFieldError
import com.enricog.features.routines.detail.routine.models.RoutineInputs
import javax.inject.Inject

internal class RoutineValidator @Inject constructor() {

    fun validate(inputs: RoutineInputs): Map<RoutineField, RoutineFieldError> {
        return buildMap {
            if (inputs.name.text.isBlank()) {
                put(RoutineField.Name, RoutineFieldError.BlankRoutineName)
            }
            if ((inputs.rounds.text.toIntOrNull() ?: 0) < Routine.MIN_NUM_ROUNDS) {
                put(RoutineField.Rounds, RoutineFieldError.BlankRoutineRounds)
            }
            if (inputs.frequencyGoal is RoutineInputs.FrequencyGoalInput.Value) {
                if ((inputs.frequencyGoal.frequencyGoalTimes.text.toIntOrNull() ?: 0) < FrequencyGoal.MIN_TIMES) {
                    put(RoutineField.FrequencyGoalTimes, RoutineFieldError.BlankFrequencyGoalTimes)
                }
            }
        }
    }
}

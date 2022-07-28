package com.enricog.features.routines.detail.segment

import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.ID
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.ui.components.extensions.toTextFieldValue
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText
import javax.inject.Inject

internal class SegmentReducer @Inject constructor() {

    private val timeTypes = listOf(TimeType.TIMER, TimeType.REST, TimeType.STOPWATCH)

    fun setup(routine: Routine, segmentId: ID): SegmentState {
        val segment = routine.segments.find { it.id == segmentId }
            ?: Segment.create(routine.getNewSegmentRank())
        val inputs = SegmentInputs(
            name = segment.name.toTextFieldValue(),
            time = segment.time.timeText,
            type = segment.type
        )
        return SegmentState.Data(
            routine = routine,
            segment = segment,
            errors = emptyMap(),
            timeTypes = timeTypes,
            inputs = inputs
        )
    }

    fun updateSegmentName(
        state: SegmentState.Data,
        textFieldValue: TextFieldValue
    ): SegmentState.Data {
        val inputs = state.inputs.copy(name = textFieldValue)
        val errors = state.errors.filterKeys { it != SegmentField.Name }
        return state.copy(inputs = inputs, errors = errors)
    }

    fun updateSegmentTime(state: SegmentState.Data, text: TimeText): SegmentState.Data {
        val time = when {
            state.inputs.type == TimeType.STOPWATCH -> "".timeText
            text.toSeconds() > 3600.seconds -> state.inputs.time
            else -> text
        }

        val inputs = state.inputs.copy(time = time)
        val errors = state.errors.filterKeys { it != SegmentField.Time }
        return state.copy(inputs = inputs, errors = errors)
    }

    fun updateSegmentTimeType(state: SegmentState.Data, timeType: TimeType): SegmentState.Data {
        if (state.inputs.type == timeType) return state

        val time = if (timeType == TimeType.STOPWATCH) {
            "".timeText
        } else {
            state.inputs.time
        }

        val inputs = state.inputs.copy(type = timeType, time = time)
        val errors = state.errors.filterKeys { it != SegmentField.Time }
        return state.copy(inputs = inputs, errors = errors)
    }

    fun applySegmentErrors(
        state: SegmentState.Data,
        errors: Map<SegmentField, SegmentFieldError>
    ): SegmentState.Data {
        return state.copy(errors = errors)
    }
}

package com.enricog.features.routines.detail.segment

import com.enricog.base.viewmodel.StateConverter
import com.enricog.core.compose.api.classes.asImmutableList
import com.enricog.core.compose.api.classes.asImmutableMap
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentState
import com.enricog.features.routines.detail.segment.models.SegmentState.Data.Action
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.segment.models.SegmentViewState.Data.Message
import com.enricog.features.routines.detail.ui.time_type.TimeTypeStyle
import javax.inject.Inject
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

internal class SegmentStateConverter @Inject constructor() :
    StateConverter<SegmentState, SegmentViewState> {

    override suspend fun convert(state: SegmentState): SegmentViewState {
        return when (state) {
            SegmentState.Idle -> SegmentViewState.Idle
            is SegmentState.Data -> state.toViewState()
            is SegmentState.Error -> SegmentViewState.Error(throwable = state.throwable)
        }
    }

    private fun SegmentState.Data.toViewState(): SegmentViewState {
        return SegmentViewState.Data(
            isTimeFieldVisible = selectedTimeType != TimeTypeEntity.STOPWATCH,
            errors = errors.asImmutableMap(),
            selectedTimeTypeStyle = TimeTypeStyle.from(timeType = selectedTimeType, timerTheme = timerTheme),
            timeTypeStyles = timeTypes.map { TimeTypeStyle.from(timeType = it, timerTheme = timerTheme) }
                .asImmutableList(),
            message = action?.toMessage()
        )
    }

    private fun Action.toMessage(): Message {
        return when (this) {
            Action.SaveSegmentError -> Message(
                textResId = R.string.label_segment_save_error,
                actionTextResId = R.string.action_text_segment_save_error
            )
        }
    }
}

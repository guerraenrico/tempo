package com.enricog.routines.detail.summary

import com.enricog.base.viewmodel.StateConverter
import com.enricog.routines.R
import com.enricog.routines.detail.summary.models.RoutineSummaryField
import com.enricog.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.routines.detail.summary.models.RoutineSummaryState
import com.enricog.routines.detail.summary.models.RoutineSummaryViewState
import javax.inject.Inject

internal class RoutineSummaryStateConverter @Inject constructor() :
    StateConverter<RoutineSummaryState, RoutineSummaryViewState> {

    override suspend fun convert(state: RoutineSummaryState): RoutineSummaryViewState {
        return when (state) {
            RoutineSummaryState.Idle -> RoutineSummaryViewState.Idle
            is RoutineSummaryState.Data -> state.toViewState()
        }
    }

    private fun RoutineSummaryState.Data.toViewState(): RoutineSummaryViewState.Data {
        val items: List<RoutineSummaryItem> = buildList {
            add(RoutineSummaryItem.RoutineInfo(routineName = routine.name))

            val mappedErrors = errors.mapValues { it.value.stringResourceId }
            add(
                RoutineSummaryItem.SegmentSectionTitle(
                    error = mappedErrors[RoutineSummaryField.Segments]?.let {
                        RoutineSummaryField.Segments to it
                    }
                )
            )

            addAll(
                routine.segments.map {
                    RoutineSummaryItem.SegmentItem(segment = it)
                }
            )
            add(RoutineSummaryItem.Space)
        }
        return RoutineSummaryViewState.Data(items = items)
    }

    private val RoutineSummaryFieldError.stringResourceId: Int
        get() {
            return when (this) {
                RoutineSummaryFieldError.NoSegments -> R.string.field_error_message_routine_no_segments
            }
        }
}

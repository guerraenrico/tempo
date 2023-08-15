package com.enricog.features.routines.detail.summary

import com.enricog.core.entities.ID
import com.enricog.core.entities.asID
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.enricog.data.timer.api.theme.entities.TimerTheme
import com.enricog.data.timer.testing.theme.entities.DEFAULT
import com.enricog.features.routines.detail.summary.models.RoutineSummaryField
import com.enricog.features.routines.detail.summary.models.RoutineSummaryFieldError
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState
import com.enricog.features.routines.detail.summary.models.RoutineSummaryState.Data.Action
import com.enricog.features.routines.detail.summary.test.RoutineSummaryStateData
import com.enricog.features.routines.detail.summary.test.RoutineSummaryStateError
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class RoutineSummaryReducerTest {

    private val reducer = RoutineSummaryReducer()

    @Test
    fun `should setup data state`() {
        val timerTheme = TimerTheme.DEFAULT
        val routine = Routine.EMPTY
        val statistics = listOf(Statistic.EMPTY)
        val state = RoutineSummaryState.Idle
        val expected = RoutineSummaryStateData {
            this.routine = routine
            this.errors = emptyMap()
            this.statistics = statistics
            this.action = null
            this.timerTheme = timerTheme
        }

        val actual = reducer.setup(state = state, routine = routine, timerTheme = timerTheme, statistics = statistics)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should setup state updating the routine when state is data`() {
        val statistics = listOf(Statistic.EMPTY)
        val timerTheme = TimerTheme.DEFAULT
        val routine = Routine.EMPTY.copy(id = ID.from(value = 2))
        val state = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(id = ID.from(value = 1))
            this.errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments)
            this.action = Action.DeleteSegmentSuccess
        }
        val expected = RoutineSummaryStateData {
            this.routine = routine
            this.errors = mapOf(RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments)
            this.action = Action.DeleteSegmentSuccess
        }

        val actual = reducer.setup(state = state, routine = routine, timerTheme = timerTheme, statistics = statistics)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should set error state`() {
        val exception = Exception("Something went wrong")
        val expected = RoutineSummaryStateError {
            throwable = exception
        }

        val actual = reducer.error(throwable = exception)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply segment errors in state`() {
        val errors = mapOf<RoutineSummaryField, RoutineSummaryFieldError>(
            RoutineSummaryField.Segments to RoutineSummaryFieldError.NoSegments
        )
        val state = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(Segment.EMPTY.copy(id = 1.asID)))
        }
        val expected = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(Segment.EMPTY.copy(id = 1.asID)))
            this.errors = errors
        }

        val actual = reducer.applyRoutineErrors(state = state, errors = errors)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply delete segment error action`() {
        val segmentId = 1.asID
        val segment = Segment.EMPTY.copy(id = segmentId)
        val state = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = null
        }
        val expected = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = Action.DeleteSegmentError(segmentId = segmentId)
        }

        val actual = reducer.deleteSegmentError(state = state, segmentId = segmentId)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply delete segment success action`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = null
        }
        val expected = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = Action.DeleteSegmentSuccess
        }

        val actual = reducer.deleteSegmentSuccess(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply move segment error action`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = null
        }
        val expected = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = Action.MoveSegmentError
        }

        val actual = reducer.moveSegmentError(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should apply duplicate segment error action`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = null
        }
        val expected = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = Action.DuplicateSegmentError
        }

        val actual = reducer.duplicateSegmentError(state = state)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should cleanup action when handled`() {
        val segment = Segment.EMPTY.copy(id = 1.asID)
        val state = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = Action.MoveSegmentError
        }
        val expected = RoutineSummaryStateData {
            this.routine = Routine.EMPTY.copy(segments = listOf(segment))
            this.action = null
        }

        val actual = reducer.actionHandled(state = state)

        assertThat(actual).isEqualTo(expected)
    }
}

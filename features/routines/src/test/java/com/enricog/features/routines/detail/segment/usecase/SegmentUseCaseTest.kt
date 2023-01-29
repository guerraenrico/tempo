package com.enricog.features.routines.detail.segment.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class SegmentUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val segment = Segment.EMPTY.copy(
        id = 1.asID,
        name = "Segment Name",
        time = 30.seconds,
        type = TimeType.TIMER
    )
    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        segments = listOf(segment)
    )
    private val store = FakeStore(listOf(routine))

    private val sut = SegmentUseCase(routineDataSource = FakeRoutineDataSource(store))

    @Test
    fun `should get routine`() = coroutineRule {
        val actual = sut.get(1.asID)

        assertThat(actual).isEqualTo(routine)
    }

    @Test
    fun `should add segment and save the routine when save with new segment`() = coroutineRule {
        val newSegment = Segment.EMPTY.copy(
            id = ID.new(),
            name = "New Segment Name",
            time = 10.seconds,
            type = TimeType.TIMER
        )
        val expected = routine.copy(segments = listOf(segment, newSegment.copy(ID.from(value = 2))))

        sut.save(routine = routine, segment = newSegment)

        assertThat(store.get().first()).isEqualTo(expected)
    }

    @Test
    fun `should update segment and save the routine when save existing segment`() = coroutineRule {
        val updatedSegment = segment.copy(name = "Updated Segment Name",)
        val expected = routine.copy(segments = listOf(updatedSegment))

        sut.save(routine = routine, segment = updatedSegment)

        assertThat(store.get().first()).isEqualTo(expected)
    }
}

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
import io.mockk.coEvery
import io.mockk.coVerify
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SegmentUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val segment = Segment.EMPTY.copy(
        id = 2.asID,
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

        assertEquals(routine, actual)
    }

    @Test
    fun `should add segment and save the routine when save with new segment`() = coroutineRule {
        val newSegment = Segment.EMPTY.copy(
            id = ID.new(),
            name = "New Segment Name",
            time = 10.seconds,
            type = TimeType.TIMER
        )
        val expected = routine.copy(segments = listOf(segment, newSegment))

        sut.save(routine = routine, segment = newSegment)

        assertEquals(expected, store.get().first())
    }

    @Test
    fun `should update segment and save the routine when save existing segment`() = coroutineRule {
        val updatedSegment = segment.copy(name = "Updated Segment Name",)
        val expected = routine.copy(segments = listOf(updatedSegment))

        sut.save(routine = routine, segment = updatedSegment)

        assertEquals(expected, store.get().first())
    }
}

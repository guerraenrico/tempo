package com.enricog.features.routines.detail.summary.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.asID
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class MoveSegmentUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val segment1 = Segment.EMPTY.copy(id = ID.from(1), rank = Rank.from("bbbbbb"))
    private val segment2 = Segment.EMPTY.copy(id = ID.from(2), rank = Rank.from("cccccc"))
    private val segment3 = Segment.EMPTY.copy(id = ID.from(3), rank = Rank.from("dddddd"))
    private val routine =
        Routine.EMPTY.copy(id = ID.from(1), segments = listOf(segment1, segment2, segment3))

    private val store = FakeStore(listOf(routine))
    private val sut = MoveSegmentUseCase(routineDataSource = FakeRoutineDataSource(store))


    @Test
    fun `should do nothing when item has not been moved`() = coroutineRule {
        sut(routine = routine, draggedSegmentId = 1.asID, hoveredSegmentId = 1.asID)

        assertEquals(routine, store.get().first())
    }

    @Test
    fun `should do nothing when moved segment is not found`() = coroutineRule {
        val segmentIdUnknown = 4.asID
        sut(routine = routine, draggedSegmentId = segmentIdUnknown, hoveredSegmentId = 1.asID)

        assertEquals(routine, store.get().first())
    }

    @Test
    fun `should move segment`() = coroutineRule {
        val expected = routine.copy(
            segments = listOf(
                segment1.copy(rank = Rank.from("oooooo")),
                segment2,
                segment3,
            )
        )

        sut(routine = routine, draggedSegmentId = 1.asID, hoveredSegmentId = 3.asID)

        assertEquals(expected, store.get().first())
    }

    @Test
    fun `should move segment to the top when hovered segment is null`() = coroutineRule {
        val expected = routine.copy(
            segments = listOf(
                segment1,
                segment2,
                segment3.copy(rank = Rank.from("annnnn"))
            )
        )

        sut(routine = routine, draggedSegmentId = 3.asID, hoveredSegmentId = null)

        assertEquals(expected, store.get().first())
    }
}
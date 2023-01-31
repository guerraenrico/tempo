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
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class MoveSegmentUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val segment1 =
        Segment.EMPTY.copy(id = ID.from(value = 1), rank = Rank.from(value = "bbbbbb"))
    private val segment2 =
        Segment.EMPTY.copy(id = ID.from(value = 2), rank = Rank.from(value = "cccccc"))
    private val segment3 =
        Segment.EMPTY.copy(id = ID.from(value = 3), rank = Rank.from(value = "dddddd"))
    private val routine = Routine.EMPTY.copy(
        id = ID.from(value = 1),
        segments = listOf(segment1, segment2, segment3)
    )

    private val store = FakeStore(listOf(routine))
    private val routineDataSource = FakeRoutineDataSource(store)
    private val useCase = MoveSegmentUseCase(routineDataSource = routineDataSource)

    @Test
    fun `should do nothing when segment has not been moved`() = coroutineRule {
        useCase(routine = routine, draggedSegmentId = segment1.id, hoveredSegmentId = segment1.id)

        assertThat(getActual()).isEqualTo(routine)
    }

    @Test
    fun `should do nothing when dragged segment is not found`() = coroutineRule {
        val segmentIdUnknown = 4.asID
        useCase(routine = routine, draggedSegmentId = segmentIdUnknown, hoveredSegmentId = segment1.id)

        assertThat(getActual()).isEqualTo(routine)
    }

    @Test
    fun `should do nothing when hovered segment is not found`() = coroutineRule {
        val segmentIdUnknown = 4.asID
        useCase(routine = routine, draggedSegmentId = segment1.id, hoveredSegmentId = segmentIdUnknown)

        assertThat(getActual()).isEqualTo(routine)
    }

    @Test
    fun `should move segment up replacing the hovered segment`() = coroutineRule {
        val expected = routine.copy(
            segments = listOf(
                segment1,
                segment3.copy(rank = Rank.from(value = "booooo")),
                segment2
            )
        )

        useCase(routine = routine, draggedSegmentId = segment3.id, hoveredSegmentId = segment2.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    @Test
    fun `should move segment down replacing the hovered segment`() = coroutineRule {
        val expected = routine.copy(
            segments = listOf(
                segment2,
                segment1.copy(rank = Rank.from(value = "cppppp")),
                segment3
            )
        )

        useCase(routine = routine, draggedSegmentId = segment1.id, hoveredSegmentId = segment2.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    @Test
    fun `should move segment to the top when hovered first segment`() = coroutineRule {
        val expected = routine.copy(
            segments = listOf(
                segment3.copy(rank = Rank.from(value = "annnnn")),
                segment1,
                segment2
            )
        )

        useCase(routine = routine, draggedSegmentId = segment3.id, hoveredSegmentId = segment1.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    @Test
    fun `should move segment to the bottom when hovered last segment`() = coroutineRule {
        val expected = routine.copy(
            segments = listOf(
                segment2,
                segment3,
                segment1.copy(rank = Rank.from(value = "oooooo"))
            )
        )

        useCase(routine = routine, draggedSegmentId = segment1.id, hoveredSegmentId = segment3.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    private suspend fun getActual(): Routine = routineDataSource.get(routine.id)
}

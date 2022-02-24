package com.enricog.features.routines.detail.summary.usecase

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.api.RoutineDataSource
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MoveSegmentUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routineDataSource: RoutineDataSource = mockk()

    private val sut = MoveSegmentUseCase(routineDataSource = routineDataSource)

    private val segment1 = Segment.EMPTY.copy(id = ID.from(1), rank = Rank.from("bbbbbb"))
    private val segment2 = Segment.EMPTY.copy(id = ID.from(2), rank = Rank.from("cccccc"))
    private val segment3 = Segment.EMPTY.copy(id = ID.from(3), rank = Rank.from("dddddd"))
    private val routine = Routine.EMPTY.copy(segments = listOf(segment1, segment2, segment3))

    @Before
    fun setup() {
        coEvery { routineDataSource.update(any()) } returns ID.from(1)
    }

    @Test
    fun `should do nothing when item has not been moved`() = coroutineRule {
        sut(routine, segment1, segment1)

        coVerify(exactly = 0) { routineDataSource.update(any()) }
    }

    @Test
    fun `should do nothing when moved segment is not found`() = coroutineRule {
        val segmentUnknown = Segment.EMPTY.copy(id = ID.from(4), rank = Rank.from("asdfgh"))
        sut(routine = routine, segment = segmentUnknown, hoveredSegment = segment1)

        coVerify(exactly = 0) { routineDataSource.update(any()) }
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

        sut(routine = routine, segment = segment1, hoveredSegment = segment3)

        coVerify { routineDataSource.update(expected) }
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

        sut(routine = routine, segment = segment3, hoveredSegment = null)

        coVerify { routineDataSource.update(expected) }
    }
}
package com.enricog.features.routines.detail.summary.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class DuplicateSegmentUseCaseTest {

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
        rank = Rank.from(value = "dddddd"),
        segments = listOf(segment1, segment2, segment3)
    )

    private val store = FakeStore(listOf(routine))
    private val routineDataSource = FakeRoutineDataSource(store)
    private val useCase = DuplicateSegmentUseCase(routineDataSource = routineDataSource)

    @Test
    fun `should do nothing when segment id is not valid`() = coroutineRule {
        val segmentIdUnknown = ID.from(value = 100)

        useCase(routine = routine, segmentId = segmentIdUnknown)

        assertThat(getActual()).isEqualTo(routine)
    }

    @Test
    fun `should duplicate segment and placed after duplicated segment when segment id is valid`() =
        coroutineRule {
            val expected = routine.copy(
                segments = listOf(
                    segment1,
                    segment1.copy(id = ID.from(value = 4), rank = Rank.from(value = "booooo")),
                    segment2,
                    segment3
                )
            )

            useCase(routine = routine, segmentId = segment1.id)

            assertThat(getActual()).isEqualTo(expected)
        }

    @Test
    fun `should duplicate segment and placed at the end when duplicated segment is the last one`() =
        coroutineRule {
            val expected = routine.copy(
                segments = listOf(
                    segment1,
                    segment2,
                    segment3,
                    segment3.copy(id = ID.from(value = 4), rank = Rank.from(value = "oooooo")),
                )
            )

            useCase(routine = routine, segmentId = segment3.id)

            assertThat(getActual()).isEqualTo(expected)
        }

    private suspend fun getActual(): Routine = routineDataSource.get(routine.id)
}
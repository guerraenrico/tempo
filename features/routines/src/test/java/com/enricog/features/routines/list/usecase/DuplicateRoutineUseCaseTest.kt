package com.enricog.features.routines.list.usecase

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


internal class DuplicateRoutineUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val segment1 =
        Segment.EMPTY.copy(id = ID.from(value = 1), rank = Rank.from(value = "bbbbbb"))
    private val segment2 =
        Segment.EMPTY.copy(id = ID.from(value = 2), rank = Rank.from(value = "cccccc"))
    private val segment3 =
        Segment.EMPTY.copy(id = ID.from(value = 3), rank = Rank.from(value = "dddddd"))

    private val routine1 =
        Routine.EMPTY.copy(id = ID.from(value = 1), rank = Rank.from(value = "bbbbbb"))
    private val routine2 =
        Routine.EMPTY.copy(id = ID.from(value = 2), rank = Rank.from(value = "cccccc"))
    private val routine3 = Routine.EMPTY.copy(
        id = ID.from(value = 3),
        rank = Rank.from(value = "dddddd"),
        segments = listOf(segment1, segment2, segment3)
    )
    private val routine4 =
        Routine.EMPTY.copy(id = ID.from(value = 4), rank = Rank.from(value = "eeeeee"))
    private val routines = listOf(routine1, routine2, routine3, routine4)

    private val store = FakeStore(routines)
    private val routineDataSource = FakeRoutineDataSource(store)
    private val useCase = DuplicateRoutineUseCase(routineDataSource = routineDataSource)

    @Test
    fun `should do nothing when routine id is not valid`() = coroutineRule {
        val routineIdUnknown = ID.from(value = 100)

        useCase(routines = routines, routineId = routineIdUnknown)

        assertThat(getActual()).isEqualTo(routines)
    }

    @Test
    fun `should duplicate routine and placed after the duplicated routine when routine id is valid`() =
        coroutineRule {
            val expected = listOf(
                routine1,
                routine1.copy(id = ID.from(value = 5), rank = Rank.from("booooo")),
                routine2,
                routine3,
                routine4,
            )

            useCase(routines = routines, routineId = routine1.id)

            assertThat(getActual()).isEqualTo(expected)
        }

    @Test
    fun `should duplicate routine and placed at the end when the duplicated routine is the last one`() =
        coroutineRule {
            val expected = listOf(
                routine1,
                routine2,
                routine3,
                routine4,
                routine4.copy(id = ID.from(value = 5), rank = Rank.from("pccccb")),
            )

            useCase(routines = routines, routineId = routine4.id)

            assertThat(getActual()).isEqualTo(expected)
        }

    @Test
    fun `should duplicate routine and segments when selected routine has segments`() =
        coroutineRule {
            val expected = listOf(
                routine1,
                routine2,
                routine3,
                routine3.copy(
                    id = ID.from(value = 5),
                    rank = Rank.from(value = "dqqqqq"),
                    segments = listOf(
                        segment1.copy(rank = Rank.from(value = "mzzzzz")),
                        segment2.copy(rank = Rank.from(value = "tmzzzz")),
                        segment3.copy(rank = Rank.from(value = "wtmzzz"))
                    )
                ),
                routine4
            )

            useCase(routines = routines, routineId = routine3.id)

            assertThat(getActual()).isEqualTo(expected)
        }

    private suspend fun getActual(): List<Routine> = routineDataSource.getAll()
}
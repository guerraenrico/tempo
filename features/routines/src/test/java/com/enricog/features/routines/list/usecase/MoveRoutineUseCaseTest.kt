package com.enricog.features.routines.list.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class MoveRoutineUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routine1 =
        Routine.EMPTY.copy(id = ID.from(value = 1), rank = Rank.from(value = "bbbbbb"))
    private val routine2 =
        Routine.EMPTY.copy(id = ID.from(value = 2), rank = Rank.from(value = "cccccc"))
    private val routine3 =
        Routine.EMPTY.copy(id = ID.from(value = 3), rank = Rank.from(value = "dddddd"))
    private val routines = listOf(routine1, routine2, routine3)

    private val store = FakeStore(routines)
    private val routineDataSource = FakeRoutineDataSource(store)
    private val moveRoutineUseCase = MoveRoutineUseCase(routineDataSource = routineDataSource)

    @Test
    fun `should do nothing when routine has not been moved`() = coroutineRule {
        moveRoutineUseCase(routines = routines, draggedRoutineId = routine1.id, hoveredRoutineId = routine1.id)

        assertThat(getActual()).isEqualTo(routines)
    }

    @Test
    fun `should do nothing when dragged routine is not found`() = coroutineRule {
        val routineIdUnknown = 4.asID

        moveRoutineUseCase(
            routines = routines,
            draggedRoutineId = routineIdUnknown,
            hoveredRoutineId = routine1.id
        )

        assertThat(getActual()).isEqualTo(routines)
    }

    @Test
    fun `should do nothing when hovered routine is not found`() = coroutineRule {
        val routineIdUnknown = 4.asID
        moveRoutineUseCase(
            routines = routines,
            draggedRoutineId = routine1.id,
            hoveredRoutineId = routineIdUnknown
        )

        assertThat(getActual()).isEqualTo(routines)
    }

    @Test
    fun `should move routine up replacing the hovered routine`() = coroutineRule {
        val expected = listOf(
            routine1,
            routine3.copy(rank = Rank.from(value = "booooo")),
            routine2
        )

        moveRoutineUseCase(routines = routines, draggedRoutineId = routine3.id, hoveredRoutineId = routine2.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    @Test
    fun `should move routine down replacing the hovered routine`() = coroutineRule {
        val expected = listOf(
            routine2,
            routine1.copy(rank = Rank.from(value = "cppppp")),
            routine3
        )

        moveRoutineUseCase(routines = routines, draggedRoutineId = routine1.id, hoveredRoutineId = routine2.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    @Test
    fun `should move routine to the top when hovered first routine`() = coroutineRule {
        val expected = listOf(
            routine3.copy(rank = Rank.from(value = "annnnn")),
            routine1,
            routine2
        )

        moveRoutineUseCase(routines = routines, draggedRoutineId = routine3.id, hoveredRoutineId = routine1.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    @Test
    fun `should move routine to the bottom when hovered last routine`() = coroutineRule {
        val expected = listOf(
            routine2,
            routine3,
            routine1.copy(rank = Rank.from(value = "oooooo"))
        )

        moveRoutineUseCase(routines = routines, draggedRoutineId = routine1.id, hoveredRoutineId = routine3.id)

        assertThat(getActual()).isEqualTo(expected)
    }

    private suspend fun getActual(): List<Routine> = routineDataSource.getAll()
}
package com.enricog.features.routines.detail.routine.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.ID
import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

class RoutineUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun `test get should return new routine with initial rank when routine id is new and there are no saved routines`() =
        coroutineRule {
            val store = FakeStore(emptyList<Routine>())
            val expected = Routine.EMPTY.copy(
                id = 0.asID,
                rank = Rank.from(value = "mzzzzz")
            )
            val routineId = ID.new()
            val useCase = buildUseCase(store = store)

            val actual = useCase.get(routineId)

            assertThat(actual.id).isEqualTo(expected.id)
            assertThat(actual.name).isEqualTo(expected.name)
            assertThat(actual.preparationTime).isEqualTo(expected.preparationTime)
            assertThat(actual.segments).isEqualTo(expected.segments)
            assertThat(actual.rank).isEqualTo(expected.rank)
        }

    @Test
    fun `test get should return new routine with rank on top when routine id is new and there are saved routines`() =
        coroutineRule {
            val store = FakeStore(
                listOf(
                    Routine.EMPTY.copy(
                        id = 1.asID,
                        name = "Routine Name",
                        rank = Rank.from(value = "mzzzzz")
                    )
                )
            )
            val expected = Routine.EMPTY.copy(
                id = 0.asID,
                rank = Rank.from(value = "gmzzzz")
            )
            val routineId = ID.new()
            val useCase = buildUseCase(store = store)

            val actual = useCase.get(routineId)

            assertThat(actual.id).isEqualTo(expected.id)
            assertThat(actual.name).isEqualTo(expected.name)
            assertThat(actual.preparationTime).isEqualTo(expected.preparationTime)
            assertThat(actual.segments).isEqualTo(expected.segments)
            assertThat(actual.rank).isEqualTo(expected.rank)
        }

    @Test
    fun `test get should return a routine when routine id is not new`() = coroutineRule {
        val expected = Routine.EMPTY.copy(id = 1.asID)
        val store = FakeStore(listOf(expected))
        val useCase = buildUseCase(store)

        val actual = useCase.get(1.asID)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `test save should create a routine if routine id is new`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = ID.new())
        val store = FakeStore(emptyList<Routine>())
        val useCase = buildUseCase(store)

        val actual = useCase.save(routine)

        store.get().first().let { createdRoutine ->
            assertThat(actual).isEqualTo(createdRoutine.id)
            assertThat(createdRoutine.name).isEqualTo(routine.name)
            assertThat(createdRoutine.preparationTime).isEqualTo(routine.preparationTime)
            assertThat(createdRoutine.segments).isEqualTo(routine.segments)
            assertThat(createdRoutine.rank).isEqualTo(routine.rank)
        }
    }

    @Test
    fun `test save should update a routine if routine id is not new`() = coroutineRule {
        val expectedRoutine = Routine.EMPTY.copy(id = 1.asID, name = "Routine Name Modified")
        val existingRoutine = Routine.EMPTY.copy(id = 1.asID, name = "Routine Name")
        val store = FakeStore(listOf(existingRoutine))
        val useCase = buildUseCase(store)

        val actual = useCase.save(expectedRoutine)

        assertThat(actual).isEqualTo(expectedRoutine.id)
        store.get().first().let { updatedRoutine ->
            assertThat(updatedRoutine).isEqualTo(expectedRoutine)
        }
    }

    private fun buildUseCase(store: FakeStore<List<Routine>> = FakeStore(emptyList())): RoutineUseCase {
        return RoutineUseCase(routineDataSource = FakeRoutineDataSource(store))
    }
}

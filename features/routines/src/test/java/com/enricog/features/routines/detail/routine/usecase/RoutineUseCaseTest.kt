package com.enricog.features.routines.detail.routine.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.asID
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    @Test
    fun `test get should return new routine when routine id is new`() = coroutineRule {
        val expected = Routine.NEW
        val routineId = ID.new()
        val sut = buildSut()

        val actual = sut.get(routineId)

        assertEquals(expected.id, actual.id)
        assertEquals(expected.name, actual.name)
        assertEquals(expected.startTimeOffset, actual.startTimeOffset)
        assertEquals(expected.segments, actual.segments)
    }

    @Test
    fun `test get should return a routine when routine id is not new`() = coroutineRule {
        val expected = Routine.EMPTY.copy(id = 1.asID)
        val store = FakeStore(listOf(expected))
        val sut = buildSut(store)

        val actual = sut.get(1.asID)

        assertEquals(expected, actual)
    }

    @Test
    fun `test save should create a routine if routine id is new`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = ID.new())
        val store = FakeStore(emptyList<Routine>())
        val sut = buildSut(store)

        val actual = sut.save(routine)

        store.get().first().let { createdRoutine ->
            assertEquals(createdRoutine.id, actual)
            assertEquals(routine.name, createdRoutine.name)
            assertEquals(routine.startTimeOffset, createdRoutine.startTimeOffset)
            assertEquals(routine.segments, createdRoutine.segments)
        }
    }

    @Test
    fun `test save should update a routine if routine id is not new`() = coroutineRule {
        val expectedRoutine = Routine.EMPTY.copy(id = 1.asID, name = "Routine Name Modified")
        val existingRoutine = Routine.EMPTY.copy(id = 1.asID, name = "Routine Name")
        val store = FakeStore(listOf(existingRoutine))
        val sut = buildSut(store)

        val actual = sut.save(expectedRoutine)

        assertEquals(expectedRoutine.id, actual)
        store.get().first().let { updatedRoutine ->
            assertEquals(expectedRoutine, updatedRoutine)
        }
    }

    private fun buildSut(store: FakeStore<List<Routine>> = FakeStore(emptyList())): RoutineUseCase {
        return RoutineUseCase(routineDataSource = FakeRoutineDataSource(store))
    }
}

package com.enricog.features.routines.list.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import kotlinx.coroutines.flow.first
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutinesUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val firstRoutine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "First Routine",
    )
    private val secondRoutine = Routine.EMPTY.copy(
        id = 2.asID,
        name = "Second Routine",
    )
    private val store = FakeStore(listOf(firstRoutine, secondRoutine))

    private val sut = RoutinesUseCase(FakeRoutineDataSource(store))

    @Test
    fun `test get all`() = coroutineRule {
        val expected = listOf(firstRoutine, secondRoutine)

        val actual = sut.getAll().first()

        assertEquals(expected, actual)
    }

    @Test
    fun `test delete`() = coroutineRule {
        val expected = listOf(secondRoutine)

        sut.delete(firstRoutine)

        val actual = store.get()

        assertEquals(expected, actual)
    }
}

package com.enricog.features.routines.detail.routine.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.EMPTY
import com.enricog.entities.ID
import com.enricog.entities.asID
import com.enricog.entities.seconds
import io.mockk.Called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routineDataSource: RoutineDataSource = mockk()

    private val sut = RoutineUseCase(routineDataSource)

    @Test
    fun `test get should return new routine when routine id is new`() = coroutineRule {
        val routineId = ID.new()

        val result = sut.get(routineId)

        assertEquals(routineId, result.id)
        assertEquals("", result.name)
        assertEquals(0.seconds, result.startTimeOffset)
        assertEquals(emptyList(), result.segments)
        verify { routineDataSource wasNot Called }
    }

    @Test
    fun `test get should return a routine when routine id is not new`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1.asID)
        coEvery { routineDataSource.get(1.asID) } returns routine

        val result = sut.get(1.asID)

        assertEquals(routine, result)
        coVerify { routineDataSource.get(1.asID) }
    }

    @Test
    @Ignore("Mockk doesn't fully support returning a value classes see https://github.com/mockk/mockk/issues/152")
    fun `test save should create a routine if routine id is new`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = ID.new())
        coEvery { routineDataSource.create(routine) } returns 1.asID

        val result = sut.save(routine)

        assertEquals(1.asID, result)
        coVerify { routineDataSource.create(routine) }
    }

    @Test
    @Ignore("Mockk doesn't fully support returning a value classes see https://github.com/mockk/mockk/issues/152")
    fun `test save should update a routine if routine id is not new`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1.asID)
        coEvery { routineDataSource.update(routine) } returns 1.asID

        val result = sut.save(routine)

        assertEquals(1.asID, result)
        coVerify { routineDataSource.update(routine) }
    }
}

package com.enricog.routines.detail.routine.usecase

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import io.mockk.*
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

class RoutineUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routineDataSource: RoutineDataSource = mockk()

    private val sut = RoutineUseCase(routineDataSource)

    @Test
    fun `test get should return new routine when id == 0`() = coroutineRule {
        val routineId = 0L

        val result = sut.get(routineId)

        assertEquals(0L, result.id)
        assertEquals("", result.name)
        assertEquals(0L, result.startTimeOffsetInSeconds)
        assertEquals(emptyList(), result.segments)
        verify { routineDataSource wasNot Called }
    }

    @Test
    fun `test get should return a routine when id != 0`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1L)
        coEvery { routineDataSource.get(1L) } returns routine

        val result = sut.get(1L)

        assertEquals(routine, result)
        coVerify { routineDataSource.get(1L) }
    }

    @Test
    fun `test save should create a routine if id == 0`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 0L)
        coEvery { routineDataSource.create(routine) } returns 1L

        val result = sut.save(routine)

        assertEquals(1L, result)
        coVerify { routineDataSource.create(routine) }
    }

    @Test
    fun `test save should update a routine if id != 0`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1L)
        coEvery { routineDataSource.update(routine) } returns 1L

        val result = sut.save(routine)

        assertEquals(1L, result)
        coVerify { routineDataSource.update(routine) }
    }
}

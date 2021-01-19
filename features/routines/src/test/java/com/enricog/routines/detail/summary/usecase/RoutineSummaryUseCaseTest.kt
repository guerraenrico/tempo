package com.enricog.routines.detail.summary.usecase

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineSummaryUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routineDataSource: RoutineDataSource = mockk()

    private val sut = RoutineSummaryUseCase(routineDataSource = routineDataSource)

    @Test
    fun `should start observe routine`() = coroutineRule {
        val routine = Routine.EMPTY
        every { routineDataSource.observe(any()) } returns flowOf(routine)

        val result = sut.get(routineId = 1).first()

        assertEquals(routine, result)
        verify { routineDataSource.observe(id = 1) }
    }

    @Test
    fun `should update routine`() = coroutineRule {
        val routine = Routine.EMPTY
        coEvery { routineDataSource.update(any()) } returns 1

        sut.update(routine)

        coVerify { routineDataSource.update(routine = routine) }
    }
}
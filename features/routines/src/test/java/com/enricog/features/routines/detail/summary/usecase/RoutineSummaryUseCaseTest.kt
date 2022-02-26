package com.enricog.features.routines.detail.summary.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.EMPTY
import com.enricog.entities.asID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

        val result = sut.get(routineId = 1.asID).first()

        assertEquals(routine, result)
        verify { routineDataSource.observe(id = 1.asID) }
    }

    @Test
    fun `should update routine`() = coroutineRule {
        val routine = Routine.EMPTY
        coEvery { routineDataSource.update(any()) } returns 1.asID

        sut.update(routine)

        coVerify { routineDataSource.update(routine = routine) }
    }
}

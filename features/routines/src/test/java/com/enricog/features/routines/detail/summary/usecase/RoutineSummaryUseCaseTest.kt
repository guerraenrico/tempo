package com.enricog.features.routines.detail.summary.usecase

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.api.RoutineDataSource
import com.enricog.entities.asID
import com.enricog.entities.routines.Routine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

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

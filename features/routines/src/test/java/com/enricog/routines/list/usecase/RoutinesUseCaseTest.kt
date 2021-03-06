package com.enricog.routines.list.usecase

import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlin.test.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test

class RoutinesUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routineDataSource: RoutineDataSource = mockk(relaxUnitFun = true)

    private val sut = RoutinesUseCase(routineDataSource)

    @Test
    fun `test get all`() = coroutineRule {
        coEvery { routineDataSource.observeAll() } returns flowOf(emptyList())

        val result = sut.getAll().first()

        assertEquals(emptyList(), result)
        coVerify { routineDataSource.observeAll() }
    }

    @Test
    fun `test delete`() = coroutineRule {
        val routine = Routine.EMPTY

        sut.delete(routine)

        coVerify { routineDataSource.delete(routine) }
    }
}

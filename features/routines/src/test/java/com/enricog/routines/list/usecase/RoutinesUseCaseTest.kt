package com.enricog.routines.list.usecase

import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

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
        // needed because right now mockk doesn't fully support value classes see https://github.com/mockk/mockk/issues/152
        coEvery { routineDataSource.delete(any()) } returns Unit

        sut.delete(routine)

        coVerify { routineDataSource.delete(routine) }
    }
}

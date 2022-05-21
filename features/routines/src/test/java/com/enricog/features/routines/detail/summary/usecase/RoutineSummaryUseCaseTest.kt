package com.enricog.features.routines.detail.summary.usecase

import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

class RoutineSummaryUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "Routine Name",
        segments = emptyList()
    )
    private val store = FakeStore(listOf(routine))
    private val sut = RoutineSummaryUseCase(routineDataSource = FakeRoutineDataSource(store))

    @Test
    fun `should observe routine`() = coroutineRule {
        val expectedAfter = Routine.EMPTY.copy(
            id = 1.asID,
            name = "Routine Name 2"
        )

        sut.get(1.asID).test {
            assertEquals(routine, awaitItem())
            store.update { listOf(expectedAfter) }
            assertEquals(expectedAfter, awaitItem())
        }
    }

    @Test
    fun `should update routine`() = coroutineRule {
        val routineUpdated = Routine.EMPTY.copy(
            id = 1.asID,
            name = "Routine Name 2"
        )

        sut.update(routineUpdated)

        assertEquals(routineUpdated, store.get().first())
    }
}

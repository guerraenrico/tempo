package com.enricog.features.routines.detail.summary.usecase

import app.cash.turbine.test
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class RoutineSummaryUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "Routine Name",
        segments = listOf(
            Segment.EMPTY.copy(id = 1.asID),
            Segment.EMPTY.copy(id = 2.asID)
        )
    )
    private val store = FakeStore(listOf(routine))
    private val sut = RoutineSummaryUseCase(routineDataSource = FakeRoutineDataSource(store))

    @Test
    fun `should observe routine`() = coroutineRule {
        val expected = routine.copy(
            id = 1.asID,
            name = "Routine Name 2"
        )

        sut.get(1.asID).test {
            assertEquals(routine, awaitItem())
            store.update { listOf(expected) }
            assertEquals(expected, awaitItem())
        }
    }

    @Test
    fun `should delete segment`() = coroutineRule {
        val segment = Segment.EMPTY.copy(id = 2.asID)
        val expected = Routine.EMPTY.copy(
            id = 1.asID,
            name = "Routine Name",
            segments = listOf(
                Segment.EMPTY.copy(id = 1.asID),
            )
        )

        sut.deleteSegment(routine = routine, segment = segment)

        assertEquals(expected, store.get().first())
    }
}

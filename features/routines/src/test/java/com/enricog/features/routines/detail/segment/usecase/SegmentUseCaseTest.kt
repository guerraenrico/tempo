package com.enricog.features.routines.detail.segment.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.routines.api.RoutineDataSource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class SegmentUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val routineDataSource: RoutineDataSource = mockk()

    private val sut = SegmentUseCase(routineDataSource = routineDataSource)

    @Test
    fun `should get routine`() = coroutineRule {
        val routine = Routine.EMPTY
        coEvery { routineDataSource.get(any()) } returns routine

        val result = sut.get(1.asID)

        assertEquals(routine, result)
        coVerify { routineDataSource.get(1.asID) }
    }

    @Test
    fun `should add segment to routine#segment and update routine when saving new segment`() =
        coroutineRule {
            val routine = Routine.EMPTY.copy(segments = emptyList())
            val segment = Segment.EMPTY.copy(id = 0.asID)
            val updatedRoutine = Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(id = 0.asID))
            )
            coEvery { routineDataSource.update(any()) } returns 1.asID

            sut.save(routine = routine, segment = segment)

            coVerify { routineDataSource.update(updatedRoutine) }
        }

    @Test
    fun `should update segment in routine#segment and update routine when saving existing segment`() =
        coroutineRule {
            val routine = Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(id = 1.asID, name = "name1"))
            )
            val segment = Segment.EMPTY.copy(id = 1.asID, name = "name2")
            val updatedRoutine = Routine.EMPTY.copy(
                segments = listOf(Segment.EMPTY.copy(id = 1.asID, name = "name2"))
            )
            coEvery { routineDataSource.update(any()) } returns 1.asID

            sut.save(routine = routine, segment = segment)

            coVerify { routineDataSource.update(updatedRoutine) }
        }
}

package com.enricog.features.routines.detail.summary.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test

internal class DeleteSegmentUseCaseTest {

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
    private val deleteSegmentUseCase = DeleteSegmentUseCase(
        routineDataSource = FakeRoutineDataSource(store = store)
    )

    @Test
    fun `should delete segment`() = coroutineRule {
        val segmentId = 2.asID
        val expected = Routine.EMPTY.copy(
            id = 1.asID,
            name = "Routine Name",
            segments = listOf(
                Segment.EMPTY.copy(id = 1.asID),
            )
        )

        deleteSegmentUseCase(routine = routine, segmentId = segmentId)

        assertThat(store.get().first()).isEqualTo(expected)
    }
}
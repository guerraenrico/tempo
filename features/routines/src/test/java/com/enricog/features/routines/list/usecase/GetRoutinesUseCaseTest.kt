package com.enricog.features.routines.list.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.testing.FakeRoutineDataSource
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.core.entities.asID
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import org.junit.Rule
import org.junit.Test

internal class GetRoutinesUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val firstRoutine = Routine.EMPTY.copy(
        id = 1.asID,
        name = "First Routine",
    )
    private val secondRoutine = Routine.EMPTY.copy(
        id = 2.asID,
        name = "Second Routine",
    )
    private val store = FakeStore(listOf(firstRoutine, secondRoutine))

    private val getRoutinesUseCase = GetRoutinesUseCase(
        routineDataSource = FakeRoutineDataSource(store = store)
    )

    @Test
    fun `should return all routines`() = coroutineRule {
        val expected = listOf(firstRoutine, secondRoutine)

        val actual = getRoutinesUseCase().first()

        assertThat(actual).isEqualTo(expected)
    }
}
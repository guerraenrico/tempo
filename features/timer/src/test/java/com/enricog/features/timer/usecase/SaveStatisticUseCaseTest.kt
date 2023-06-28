package com.enricog.features.timer.usecase

import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.testing.statistics.FakeRoutineStatisticsDataSource
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.test.runCurrent
import org.junit.Rule
import org.junit.Test

internal class SaveStatisticUseCaseTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private val store = FakeStore<List<Statistic>>(initialValue = emptyList())

    @Test
    fun `should save routine statistic`() = coroutineRule {
        val routine = Routine.EMPTY.copy(id = 1.asID)
        val type = Statistic.Type.ROUTINE_COMPLETED
        val effectiveTime = 10.seconds
        val expected = Statistic.EMPTY.copy(
            id = 1.asID,
            routineId = 1.asID,
            type = type,
            effectiveTime = effectiveTime
        )
        val useCase = buildUseCase(scope = this)

        useCase(routine = routine, type = type, effectiveTime = effectiveTime)
        runCurrent()

        val actual = store.get().first()
        assertThat(actual.id).isEqualTo(expected.id)
        assertThat(actual.routineId).isEqualTo(expected.routineId)
        assertThat(actual.type).isEqualTo(expected.type)
        assertThat(actual.effectiveTime).isEqualTo(expected.effectiveTime)
    }

    private fun buildUseCase(scope: CoroutineScope): SaveStatisticUseCase {
        return SaveStatisticUseCase(
            scope = scope,
            routineStatisticsDataSource = FakeRoutineStatisticsDataSource(store = store)
        )
    }
}

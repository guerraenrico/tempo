package com.enricog.features.routines.detail.summary.usecase

import com.enricog.core.entities.asID
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.data.routines.testing.statistics.FakeRoutineStatisticsDataSource
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class GetRoutineStatisticTest {

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val statistics = listOf(
        Statistic.EMPTY.copy(
            routineId = 1.asID,
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        ),
        Statistic.EMPTY.copy(
            routineId = 2.asID,
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-02T11:05:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        ),
        Statistic.EMPTY.copy(
            routineId = 1.asID,
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-02T11:01:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_ABORTED
        ),
        Statistic.EMPTY.copy(
            routineId = 1.asID,
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-01T11:05:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        ),
        Statistic.EMPTY.copy(
            routineId = 1.asID,
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-03-31T03:43:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        )
    )

    private val statisticsDataSource = FakeRoutineStatisticsDataSource(store = FakeStore(statistics))
    private val useCase = GetRoutineStatistic(statisticsDataSource = statisticsDataSource, clock = clock)

    @Test
    fun `should get all routine statistics based on frequency goal period`() = runTest {
        val routine = Routine.EMPTY.copy(
            id = 1.asID,
            frequencyGoal = FrequencyGoal(
                times = 2,
                period = FrequencyGoal.Period.WEEK
            )
        )
        val expected = listOf(
            Statistic.EMPTY.copy(
                routineId = 1.asID,
                createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC")),
                type = Statistic.Type.ROUTINE_COMPLETED
            ),
            Statistic.EMPTY.copy(
                routineId = 1.asID,
                createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-02T11:01:30.00Z"), ZoneId.of("UTC")),
                type = Statistic.Type.ROUTINE_ABORTED
            )
        )

        val actual = useCase(routine = routine)

        assertThat(actual).isEqualTo(expected)
    }
}
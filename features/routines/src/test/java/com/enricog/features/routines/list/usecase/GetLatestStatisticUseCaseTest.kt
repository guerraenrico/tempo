package com.enricog.features.routines.list.usecase

import com.enricog.core.entities.asID
import com.enricog.data.local.testing.FakeStore
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.statistics.FakeRoutineStatisticsDataSource
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class GetLatestStatisticUseCaseTest {

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val statistics = listOf(
        Statistic.EMPTY.copy(
            routineId = 3.asID,
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-03T20:23:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_ABORTED
        ),
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
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-03-03T11:05:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        ),
        Statistic.EMPTY.copy(
            routineId = 1.asID,
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-03-02T03:43:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        )
    )

    private val statisticsDataSource = FakeRoutineStatisticsDataSource(store = FakeStore(statistics))
    private val useCase = GetLatestStatisticUseCase(routineStatisticsDataSource = statisticsDataSource, clock = clock)

    @Test
    fun `should get all latest routine statistics within one month`() = runTest {
        val expected = listOf(
            Statistic.EMPTY.copy(
                routineId = 3.asID,
                createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-03T20:23:30.00Z"), ZoneId.of("UTC")),
                type = Statistic.Type.ROUTINE_ABORTED
            ),
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
                createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-03-03T11:05:30.00Z"), ZoneId.of("UTC")),
                type = Statistic.Type.ROUTINE_COMPLETED
            )
        )

        val actual = useCase()

        assertThat(actual).isEqualTo(expected)
    }
}
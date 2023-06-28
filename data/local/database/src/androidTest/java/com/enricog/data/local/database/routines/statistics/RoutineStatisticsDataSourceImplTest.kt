package com.enricog.data.local.database.routines.statistics

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.local.database.TempoDatabase
import com.enricog.data.local.database.TestTempoDatabaseFactory
import com.enricog.data.local.database.routines.model.InternalRoutine
import com.enricog.data.local.database.routines.statistics.model.InternalStatistic
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@RunWith(AndroidJUnit4::class)
class RoutineStatisticsDataSourceImplTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var database: TempoDatabase
    private lateinit var dataSource: RoutineStatisticsDataSourceImpl

    @Before
    fun setup() {
        database = TestTempoDatabaseFactory.create()
        dataSource = RoutineStatisticsDataSourceImpl(database = database)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldGetAllStatistics() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val internalStatistic = InternalStatistic(
            id = 2,
            routineId = 1,
            createdAt = now,
            type = "ROUTINE_ABORTED",
            effectiveTime = 15
        )
        val statistic = Statistic(
            id = 2.asID,
            routineId = 1.asID,
            createdAt = now,
            type = Statistic.Type.ROUTINE_ABORTED,
            effectiveTime = 15.seconds
        )
        val expected = listOf(statistic)

        database.routineDao().insert(internalRoutine)
        database.statisticDao().insert(internalStatistic)

        val actual = dataSource.getAll()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun shouldGetAllStatisticsByRoutineId() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val internalStatistic = InternalStatistic(
            id = 2,
            routineId = 1,
            createdAt = now,
            effectiveTime = 15,
            type = "ROUTINE_ABORTED",
        )
        val statistic = Statistic(
            id = 2.asID,
            routineId = 1.asID,
            createdAt = now,
            type = Statistic.Type.ROUTINE_ABORTED,
            effectiveTime = 15.seconds
        )
        val expected = listOf(statistic)

        database.routineDao().insert(internalRoutine)
        database.statisticDao().insert(internalStatistic)

        val actual = dataSource.getAllByRoutine(routineId = 1.asID)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun shouldCreateStatistic() = coroutineRule {
        val now = OffsetDateTime.now()
        val statistic = Statistic(
            id = 0.asID,
            routineId = 1.asID,
            createdAt = now,
            type = Statistic.Type.ROUTINE_ABORTED,
            effectiveTime = 15.seconds
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val expected = Statistic(
            id = 1.asID,
            routineId = 1.asID,
            createdAt = now,
            type = Statistic.Type.ROUTINE_ABORTED,
            effectiveTime = 15.seconds
        )

        database.routineDao().insert(internalRoutine)

        val statisticId = dataSource.create(statistic = statistic)

        assertThat(statisticId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.getAllByRoutine(routineId = 1.asID)
        assertStatisticEquals(expected, result.first())
    }

    private fun assertStatisticEquals(expected: Statistic, actual: Statistic) {
        assertThat(actual.id).isEqualTo(expected.id)
        assertThat(actual.routineId).isEqualTo(expected.routineId)
        assertThat(actual.effectiveTime).isEqualTo(expected.effectiveTime)
        assertThat(actual.type).isEqualTo(expected.type)
        assertThat(actual.createdAt.truncatedTo(ChronoUnit.HOURS))
            .isEqualTo(expected.createdAt.truncatedTo(ChronoUnit.HOURS))
    }
}

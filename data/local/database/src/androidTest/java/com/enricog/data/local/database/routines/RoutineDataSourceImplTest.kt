package com.enricog.data.local.database.routines

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.database.TempoDatabase
import com.enricog.data.local.database.routines.model.InternalRoutine
import com.enricog.data.local.database.routines.model.InternalSegment
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

@RunWith(AndroidJUnit4::class)
class RoutineDataSourceImplTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private lateinit var database: TempoDatabase
    private lateinit var dataSource: RoutineDataSourceImpl

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, TempoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dataSource = RoutineDataSourceImpl(database)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun shouldObserveAllRoutines() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )
        val expected = listOf(routine)

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val actual = dataSource.observeAll().first()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun shouldObserveSingleRoutine() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val expected = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val actual = dataSource.observe(1.asID).first()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun shouldReturnAllRoutines() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )
        val expected = listOf(routine)

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val actual = dataSource.getAll()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun shouldReturnSingleRoutine() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val expected = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val actual = dataSource.get(1.asID)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun shouldCreateRoutine() = coroutineRule {
        val max = OffsetDateTime.MIN
        val now = OffsetDateTime.now()
        val routine = Routine(
            id = 0.asID,
            name = "name",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )
        val expected = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        val routineId = dataSource.create(routine)

        assertThat(routineId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.get(1.asID)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun shouldUpdateRoutine() = coroutineRule {
        val max = OffsetDateTime.MIN
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = dataSource.update(routine)

        assertThat(routineId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.get(1.asID)
        assertRoutineEquals(routine, result)
    }

    @Test
    fun shouldUpdateRoutineAndSegment() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name2",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = dataSource.update(routine)

        assertThat(routineId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.get(1.asID)
        assertRoutineEquals(routine, result)
    }

    @Test
    fun shouldUpdateRoutineAndDeleteSegment() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = emptyList(),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = dataSource.update(routine)

        assertThat(routineId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.get(1.asID)
        assertRoutineEquals(routine, result)
    }

    @Test
    fun shouldUpdateRoutineAndCreateSegment() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 0.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )
        val expected = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)

        val routineId = dataSource.update(routine)

        assertThat(routineId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.get(1.asID)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun shouldUpdateRoutineAndCreateAndDeleteSegments() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 0.asID,
                    name = "name2",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )
        val expected = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 2.asID,
                    name = "name2",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = dataSource.update(routine)

        assertThat(routineId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.get(1.asID)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun shouldUpdateRoutineCreateDeleteAndUpdateSegments() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment1 = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalSegment2 = InternalSegment(
            id = 2,
            routineId = 1,
            name = "name2",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "bbbbbb"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 2.asID,
                    name = "name2_mod",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                ),
                Segment(
                    id = 0.asID,
                    name = "name3",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "bbbbbb")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )
        val expected = Routine(
            id = 1.asID,
            name = "name2",
            preparationTime = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 2.asID,
                    name = "name2_mod",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                ),
                Segment(
                    id = 3.asID,
                    name = "name3",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "bbbbbb")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment1, internalSegment2)

        val routineId = dataSource.update(routine)

        assertThat(routineId).isEqualTo(1.asID)
        // Assert that has been saved correctly
        val result = dataSource.get(1.asID)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun shouldDeleteRoutineAndAllRelativeSegments() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1.asID,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from(value = "aaaaaa")
                )
            ),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        dataSource.delete(routine)

        val segments = database.segmentDao().getAll()
        val routines = database.routineDao().getAll()
        assertThat(segments).isEmpty()
        assertThat(routines).isEmpty()
    }

    private fun assertRoutineEquals(expected: Routine, actual: Routine) {
        assertThat(actual.id).isEqualTo(expected.id)
        assertThat(actual.name).isEqualTo(expected.name)
        assertThat(actual.preparationTime).isEqualTo(expected.preparationTime)
        assertThat(actual.segments).isEqualTo(expected.segments)
        assertThat(actual.createdAt.truncatedTo(ChronoUnit.HOURS))
            .isEqualTo(expected.createdAt.truncatedTo(ChronoUnit.HOURS))
        assertThat(actual.updatedAt.truncatedTo(ChronoUnit.HOURS))
            .isEqualTo(expected.updatedAt.truncatedTo(ChronoUnit.HOURS))
    }
}

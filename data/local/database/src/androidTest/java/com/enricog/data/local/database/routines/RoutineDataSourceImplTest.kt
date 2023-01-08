package com.enricog.data.local.database.routines

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.enricog.core.coroutines.testing.CoroutineRule
import com.enricog.data.local.database.TempoDatabase
import com.enricog.data.local.database.routines.model.InternalRoutine
import com.enricog.data.local.database.routines.model.InternalRoutineWithSegments
import com.enricog.data.local.database.routines.model.InternalSegment
import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.Rank
import com.enricog.entities.asID
import com.enricog.entities.seconds
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.assertEquals
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
    private lateinit var sut: RoutineDataSourceImpl

    @Before
    fun setup() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(context, TempoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        sut = RoutineDataSourceImpl(database)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() {
        database.close()
    }

    @Test
    fun shoulObserveAllRoutines() = coroutineRule {
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
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name",
            startTimeOffset = 10.seconds,
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

        val result = sut.observeAll().first()

        assertEquals(expected, result)
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
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val expected = Routine(
            id = 1.asID,
            name = "name",
            startTimeOffset = 10.seconds,
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

        val result = sut.observe(1.asID).first()

        assertEquals(expected, result)
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
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name",
            startTimeOffset = 10.seconds,
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

        val result = sut.getAll()

        assertEquals(expected, result)
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
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val expected = Routine(
            id = 1.asID,
            name = "name",
            startTimeOffset = 10.seconds,
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

        val result = sut.get(1.asID)

        assertEquals(expected, result)
    }

    @Test
    fun shouldCreateRoutine() = coroutineRule {
        val max = OffsetDateTime.MIN
        val now = OffsetDateTime.now()
        val routine = Routine(
            id = 0.asID,
            name = "name",
            startTimeOffset = 10.seconds,
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
            startTimeOffset = 10.seconds,
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

        val routineId = sut.create(routine)

        assertEquals(routineId, 1.asID)
        // Assert that has been saved correctly
        val result = sut.get(1.asID)
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
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            startTimeOffset = 10.seconds,
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

        val routineId = sut.update(routine)

        assertEquals(routineId, 1.asID)
        // Assert that has been saved correctly
        val result = sut.get(1.asID)
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
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            startTimeOffset = 10.seconds,
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

        val routineId = sut.update(routine)

        assertEquals(routineId, 1.asID)
        // Assert that has been saved correctly
        val result = sut.get(1.asID)
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
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = emptyList(),
            rank = Rank.from(value = "abcdef")
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = sut.update(routine)

        assertEquals(routineId, 1.asID)
        // Assert that has been saved correctly
        val result = sut.get(1.asID)
        assertRoutineEquals(routine, result)
    }

    @Test
    fun shouldUpdateRoutineAndCreateSegment() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            startTimeOffset = 10.seconds,
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
            startTimeOffset = 10.seconds,
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

        val routineId = sut.update(routine)

        assertEquals(routineId, 1.asID)
        // Assert that has been saved correctly
        val result = sut.get(1.asID)
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
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            startTimeOffset = 10.seconds,
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
            startTimeOffset = 10.seconds,
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

        val routineId = sut.update(routine)

        assertEquals(routineId, 1.asID)
        // Assert that has been saved correctly
        val result = sut.get(1.asID)
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
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name2",
            startTimeOffset = 10.seconds,
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
            startTimeOffset = 10.seconds,
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

        val routineId = sut.update(routine)

        assertEquals(routineId, 1.asID)
        // Assert that has been saved correctly
        val result = sut.get(1.asID)
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
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef"
        )
        val routine = Routine(
            id = 1.asID,
            name = "name",
            startTimeOffset = 10.seconds,
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

        sut.delete(routine)

        val segments = database.segmentDao().getAll()
        val routines = database.routineDao().getAll()
        assertEquals(emptyList<InternalSegment>(), segments)
        assertEquals(emptyList<InternalRoutineWithSegments>(), routines)
    }

    private fun assertRoutineEquals(expected: Routine, actual: Routine) {
        assertEquals(expected.id, actual.id)
        assertEquals(expected.name, actual.name)
        assertEquals(expected.startTimeOffset, actual.startTimeOffset)
        assertEquals(
            expected.createdAt.truncatedTo(ChronoUnit.HOURS),
            actual.createdAt.truncatedTo(ChronoUnit.HOURS)
        )
        assertEquals(
            expected.updatedAt.truncatedTo(ChronoUnit.HOURS),
            actual.updatedAt.truncatedTo(ChronoUnit.HOURS)
        )
        assertEquals(expected.segments, actual.segments)
    }
}

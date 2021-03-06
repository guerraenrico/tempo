package com.enricog.localdatasource.routine

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.entities.seconds
import com.enricog.localdatasource.TempoDatabase
import com.enricog.localdatasource.routine.model.InternalRoutine
import com.enricog.localdatasource.routine.model.InternalRoutineWithSegments
import com.enricog.localdatasource.routine.model.InternalSegment
import java.io.IOException
import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class RoutineDataSourceImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

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
    fun `should observe all routines`() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now
        )
        val routine = Routine(
            id = 1,
            name = "name",
            startTimeOffset = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = listOf(routine)

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val result = sut.observeAll().first()

        assertEquals(expected, result)
    }

    @Test
    fun `should observe single routine`() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now
        )
        val expected = Routine(
            id = 1,
            name = "name",
            startTimeOffset = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val result = sut.observe(1).first()

        assertEquals(expected, result)
    }

    @Test
    fun `should return single routine`() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now
        )
        val expected = Routine(
            id = 1,
            name = "name",
            startTimeOffset = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val result = sut.get(1)

        assertEquals(expected, result)
    }

    @Test
    fun `should create a routine`() = coroutineRule {
        val max = OffsetDateTime.MIN
        val now = OffsetDateTime.now()
        val routine = Routine(
            id = 0,
            name = "name",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name",
            startTimeOffset = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        val routineId = sut.create(routine)

        assertEquals(routineId, 1L)
        // Assert that has been saved correctly
        val result = sut.get(1)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun `should update routine`() = coroutineRule {
        val max = OffsetDateTime.MIN
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = sut.update(routine)

        assertEquals(routineId, 1L)
        // Assert that has been saved correctly
        val result = sut.get(1)
        assertRoutineEquals(routine, result)
    }

    @Test
    fun `should update routine and segment`() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name2",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = sut.update(routine)

        assertEquals(routineId, 1L)
        // Assert that has been saved correctly
        val result = sut.get(1)
        assertRoutineEquals(routine, result)
    }

    @Test
    fun `should update routine and delete segment`() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = emptyList()
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = sut.update(routine)

        assertEquals(routineId, 1L)
        // Assert that has been saved correctly
        val result = sut.get(1)
        assertRoutineEquals(routine, result)
    }

    @Test
    fun `should update routine and create segment`() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 0,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)

        val routineId = sut.update(routine)

        assertEquals(routineId, 1L)
        // Assert that has been saved correctly
        val result = sut.get(1)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun `should update routine and create and delete segments`() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 0,
                    name = "name2",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 2,
                    name = "name2",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val routineId = sut.update(routine)

        assertEquals(routineId, 1L)
        // Assert that has been saved correctly
        val result = sut.get(1)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun `should update routine and create, delete and update segments`() = coroutineRule {
        val max = OffsetDateTime.MAX
        val now = OffsetDateTime.now()
        val internalSegment1 = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalSegment2 = InternalSegment(
            id = 2,
            routineId = 1,
            name = "name2",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = max,
            updatedAt = max
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = max,
            segments = listOf(
                Segment(
                    id = 2,
                    name = "name2_mod",
                    time = 40.seconds,
                    type = TimeType.TIMER
                ),
                Segment(
                    id = 0,
                    name = "name3",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name2",
            startTimeOffset = 10.seconds,
            createdAt = max,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 2,
                    name = "name2_mod",
                    time = 40.seconds,
                    type = TimeType.TIMER
                ),
                Segment(
                    id = 3,
                    name = "name3",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment1, internalSegment2)

        val routineId = sut.update(routine)

        assertEquals(routineId, 1L)
        // Assert that has been saved correctly
        val result = sut.get(1)
        assertRoutineEquals(expected, result)
    }

    @Test
    fun `should delete routine and all relative segments`() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 1,
            name = "name",
            timeInSeconds = 40,
            type = TimeType.TIMER
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now
        )
        val routine = Routine(
            id = 1,
            name = "name",
            startTimeOffset = 10.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    time = 40.seconds,
                    type = TimeType.TIMER
                )
            )
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

package com.enricog.localdatasource.routine

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import com.enricog.localdatasource.TempoDatabase
import com.enricog.localdatasource.routine.model.InternalRoutine
import com.enricog.localdatasource.routine.model.InternalSegment
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.IOException
import java.time.OffsetDateTime

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
    fun `should return all routines`() = coroutineRule {
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
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = listOf(routine)

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        val result = sut.getAll()

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
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    timeInSeconds = 40,
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
        val now = OffsetDateTime.now()
        val routine = Routine(
            id = 0,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )

        sut.create(routine)

        // Assert that has been saved correctly
        val result = sut.get(1)
        assertEquals(expected, result)
    }

    @Test
    fun `should update routine`() = coroutineRule {
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
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        sut.update(routine)

        // Assert that has been saved correctly
        val result = sut.get(1)
        assertEquals(routine, result)
    }

    @Test
    fun `should update routine and segment`() = coroutineRule {
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
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name2",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        sut.update(routine)

        // Assert that has been saved correctly
        val result = sut.get(1)
        assertEquals(routine, result)
    }

    @Test
    fun `should update routine and delete segment`() = coroutineRule {
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
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = emptyList()
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        sut.update(routine)

        // Assert that has been saved correctly
        val result = sut.get(1)
        assertEquals(routine, result)
    }

    @Test
    fun `should update routine and create segment`() = coroutineRule {
        val now = OffsetDateTime.now()
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 0,
                    name = "name",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 1,
                    name = "name",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)

        sut.update(routine)

        // Assert that has been saved correctly
        val result = sut.get(1)
        assertEquals(expected, result)
    }

    @Test
    fun `should update routine and create and delete segments`() = coroutineRule {
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
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 0,
                    name = "name2",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 2,
                    name = "name2",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment)

        sut.update(routine)

        // Assert that has been saved correctly
        val result = sut.get(1)
        assertEquals(expected, result)
    }

    @Test
    fun `should update routine and create, delete and update segments`() = coroutineRule {
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
            createdAt = now,
            updatedAt = now
        )
        val routine = Routine(
            id = 1,
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 2,
                    name = "name2_mod",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                ),
                Segment(
                    id = 0,
                    name = "name3",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )
        val expected = Routine(
            id = 1,
            name = "name2",
            startTimeOffsetInSeconds = 10,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 2,
                    name = "name2_mod",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                ),
                Segment(
                    id = 3,
                    name = "name3",
                    timeInSeconds = 40,
                    type = TimeType.TIMER
                )
            )
        )

        database.routineDao().insert(internalRoutine)
        database.segmentDao().insert(internalSegment1, internalSegment2)

        sut.update(routine)

        // Assert that has been saved correctly
        val result = sut.get(1)
        assertEquals(expected, result)
    }
}
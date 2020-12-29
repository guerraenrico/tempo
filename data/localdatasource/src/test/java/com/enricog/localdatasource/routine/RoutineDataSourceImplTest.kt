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
        val routineId = 1L
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
            id = routineId,
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

        val result = sut.get(routineId)

        assertEquals(expected, result)
    }

    @Test
    fun `should create a routine`() = coroutineRule {
        val now = OffsetDateTime.now()
        val routineId = 1L
        val routine = Routine(
            id = routineId,
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
        val result = sut.get(routineId)
        assertEquals(routine, result)
    }


}
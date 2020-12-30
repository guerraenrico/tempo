package com.enricog.routines.usecase

import android.annotation.SuppressLint
import com.enricog.datasource.RoutineDataSource
import com.enricog.entities.routines.Routine
import com.enricog.entities.routines.Segment
import com.enricog.entities.routines.TimeType
import java.time.OffsetDateTime
import javax.inject.Inject

@SuppressLint("NewApi")
internal class RoutineUseCase @Inject constructor(
    private val routineDataSource: RoutineDataSource
) {

    private val newRoutine: Routine
        get() {
            val now = OffsetDateTime.now()
            return Routine(
                id = 0,
                name = "",
                startTimeOffsetInSeconds = 0,
                createdAt = now,
                updatedAt = now,
                segments = emptyList()
            )
        }

    suspend fun get(routineId: Long): Routine {
        return if (routineId == 0L) {
            newRoutine
        } else {
            val routine = Routine(
                id = 1,
                name = "Routine mocked",
                startTimeOffsetInSeconds = 5,
                createdAt = OffsetDateTime.now(),
                updatedAt = OffsetDateTime.now(),
                segments = listOf(
                    Segment(
                        id = 4,
                        name = "Segment 4",
                        timeInSeconds = 0,
                        type = TimeType.STOPWATCH
                    ),
//                Segment(id = 1, name = "Segment 1", timeInSeconds = 15, type = TimeType.TIMER),
//                Segment(id = 2, name = "Segment 2", timeInSeconds = 10, type = TimeType.REST),
//                Segment(id = 3, name = "Segment 3", timeInSeconds = 15, type = TimeType.TIMER),
//                Segment(id = 5, name = "Segment 5", timeInSeconds = 10, type = TimeType.REST),
                )
            )
            return routine
//            routineDataSource.get(routineId)
        }
    }

    suspend fun save(routine: Routine) {
        if (routine.id == 0L) {
            routineDataSource.create(routine)
        } else {
            routineDataSource.update(routine)
        }
    }
}
package com.enricog.data.local.database.routines.model

import com.enricog.data.routines.api.entities.Routine
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.data.routines.testing.entities.RANDOM
import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.OffsetDateTime

class InternalRoutineTest {

    @Test
    fun `map to internal model`() {
        val now = OffsetDateTime.now()
        val routine = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 2.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 3.asID,
                    name = "name",
                    time = 4.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.RANDOM,
                    rounds = 1
                )
            ),
            rank = Rank.from(value = "abcdef"),
            rounds = 1,
            frequencyGoal = FrequencyGoal(
                times = 2,
                period = FrequencyGoal.Period.WEEK
            )
        )
        val expected = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 2,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef",
            rounds = 1,
            frequencyGoal = InternalRoutine.InternalFrequencyGoal(
                times = 2,
                period = InternalRoutine.InternalFrequencyGoal.InternalPeriod.WEEK
            )
        )

        val actual = routine.toInternal()

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `map to entity`() {
        val now = OffsetDateTime.now()
        val internalSegments = listOf(
            InternalSegment(
                id = 3,
                routineId = 1,
                name = "name",
                timeInSeconds = 4,
                type = TimeType.TIMER,
                rank = "aaaaaa",
                rounds = 1
            )
        )
        val internalRoutine = InternalRoutine(
            id = 1,
            name = "name",
            preparationTime = 2,
            createdAt = now,
            updatedAt = now,
            rank = "abcdef",
            rounds = 1,
            frequencyGoal = InternalRoutine.InternalFrequencyGoal(
                times = 2,
                period = InternalRoutine.InternalFrequencyGoal.InternalPeriod.WEEK
            )
        )
        val expected = Routine(
            id = 1.asID,
            name = "name",
            preparationTime = 2.seconds,
            createdAt = now,
            updatedAt = now,
            segments = listOf(
                Segment(
                    id = 3.asID,
                    name = "name",
                    time = 4.seconds,
                    type = TimeType.TIMER,
                    rank = Rank.from("aaaaaa"),
                    rounds = 1
                )
            ),
            rank = Rank.from(value = "abcdef"),
            rounds = 1,
            frequencyGoal = FrequencyGoal(
                times = 2,
                period = FrequencyGoal.Period.WEEK
            )
        )

        val actual = internalRoutine.toEntity(internalSegments)

        assertThat(actual).isEqualTo(expected)
    }
}

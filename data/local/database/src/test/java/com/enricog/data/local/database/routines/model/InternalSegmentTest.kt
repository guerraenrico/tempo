package com.enricog.data.local.database.routines.model

import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.core.entities.Rank
import com.enricog.core.entities.asID
import com.enricog.core.entities.seconds
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class InternalSegmentTest {

    @Test
    fun `map to internal model`() {
        val routineId = 1L
        val segment = Segment(
            id = 2.asID,
            name = "name",
            time = 3.seconds,
            type = TimeType.TIMER,
            rank = Rank.from(value = "aaaaaa")
        )
        val expected = InternalSegment(
            id = 2,
            routineId = routineId,
            name = "name",
            timeInSeconds = 3,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )

        val actual = segment.toInternal(routineId)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `map to entity`() {
        val internalSegment = InternalSegment(
            id = 1,
            routineId = 2,
            name = "name",
            timeInSeconds = 3,
            type = TimeType.TIMER,
            rank = "aaaaaa"
        )
        val expected = Segment(
            id = 1.asID,
            name = "name",
            time = 3.seconds,
            type = TimeType.TIMER,
            rank = Rank.from(value = "aaaaaa")
        )

        val actual = internalSegment.toEntity()

        assertThat(actual).isEqualTo(expected)
    }
}

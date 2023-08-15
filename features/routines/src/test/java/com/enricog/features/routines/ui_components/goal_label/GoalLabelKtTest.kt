package com.enricog.features.routines.ui_components.goal_label

import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.data.routines.api.statistics.entities.Statistic
import com.enricog.data.routines.testing.statistics.entities.EMPTY
import com.enricog.features.routines.R
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId


class GoalLabelKtTest {

    private val clock = Clock.fixed(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC"))

    private val statistics = listOf(
        Statistic.EMPTY.copy(
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-03T10:15:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        ),
        Statistic.EMPTY.copy(
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-02T11:05:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        ),
        Statistic.EMPTY.copy(
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-02T11:01:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_ABORTED
        ),
        Statistic.EMPTY.copy(
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-04-01T11:05:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        ),
        Statistic.EMPTY.copy(
            createdAt = OffsetDateTime.ofInstant(Instant.parse("2023-03-31T03:43:30.00Z"), ZoneId.of("UTC")),
            type = Statistic.Type.ROUTINE_COMPLETED
        )
    )

    @Test
    fun `should map daily frequency goal to label`() {
        val frequencyGoal = FrequencyGoal(
            times = 2,
            period = FrequencyGoal.Period.DAY
        )
        val expected = GoalLabel(
            stringResId = R.string.label_routine_goal_text_day,
            formatArgs = immutableListOf(1, 2)
        )

        val actual = frequencyGoal.toGoalLabel(clock = clock, statistics = statistics)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map weekly frequency goal to label`() {
        val frequencyGoal = FrequencyGoal(
            times = 3,
            period = FrequencyGoal.Period.WEEK
        )
        val expected = GoalLabel(
            stringResId = R.string.label_routine_goal_text_week,
            formatArgs = immutableListOf(2, 3)
        )

        val actual = frequencyGoal.toGoalLabel(clock = clock, statistics = statistics)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `should map monthly frequency goal to label`() {
        val frequencyGoal = FrequencyGoal(
            times = 4,
            period = FrequencyGoal.Period.MONTH
        )
        val expected = GoalLabel(
            stringResId = R.string.label_routine_goal_text_month,
            formatArgs = immutableListOf(3, 4)
        )

        val actual = frequencyGoal.toGoalLabel(clock = clock, statistics = statistics)

        assertThat(actual).isEqualTo(expected)
    }
}
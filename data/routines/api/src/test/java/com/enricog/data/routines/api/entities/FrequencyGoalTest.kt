package com.enricog.data.routines.api.entities

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId

class FrequencyGoalTest {

    @Test
    fun `day time range should go from midnight plus twenty four hours`() {
        val clock = Clock.fixed(Instant.parse("2023-08-11T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-11T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-08-11T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.DAY.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }

    @Test
    fun `week time range should go from sunday to sunday`() {
        val clock = Clock.fixed(Instant.parse("2023-08-11T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-06T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-08-12T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.WEEK.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }

    @Test
    fun `week time range should be correct when current day is first day of week`() {
        val clock = Clock.fixed(Instant.parse("2023-08-06T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-06T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-08-12T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.WEEK.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }

    @Test
    fun `week time range should be correct when current day is last day of week`() {
        val clock = Clock.fixed(Instant.parse("2023-08-12T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-06T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-08-12T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.WEEK.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }

    @Test
    fun `week time range should be correct when week is split between 2 months`() {
        val clock = Clock.fixed(Instant.parse("2023-09-01T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-27T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-09-02T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.WEEK.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }

    @Test
    fun `month time range should go from first day of the month to the last`() {
        val clock = Clock.fixed(Instant.parse("2023-08-11T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-01T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-08-31T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.MONTH.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }

    @Test
    fun `month time range should be correct when today is first day of month`() {
        val clock = Clock.fixed(Instant.parse("2023-08-01T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-01T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-08-31T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.MONTH.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }

    @Test
    fun `month time range should be correct when today is last day of month`() {
        val clock = Clock.fixed(Instant.parse("2023-08-31T10:15:30.41Z"), ZoneId.of("UTC"))
        val expectedFrom = OffsetDateTime.ofInstant(Instant.parse("2023-08-01T00:00:00.00Z"), ZoneId.of("UTC"))
        val expectedTo = OffsetDateTime.ofInstant(Instant.parse("2023-08-31T23:59:59.00Z"), ZoneId.of("UTC"))

        val (actualFrom, actualTo) = FrequencyGoal.Period.MONTH.timeRange(clock = clock)

        assertThat(actualFrom).isEqualTo(expectedFrom)
        assertThat(expectedTo).isEqualTo(actualTo)
    }
}
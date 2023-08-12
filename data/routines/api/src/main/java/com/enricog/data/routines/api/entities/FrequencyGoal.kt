package com.enricog.data.routines.api.entities

import java.time.Clock
import java.time.DayOfWeek
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.Locale

data class FrequencyGoal(
    val times: Int,
    val period: Period
) {

    init {
        require(times >= MIN_TIMES) {
            "times must be equals or grater than $MIN_TIMES"
        }
    }

    enum class Period {
        DAY, WEEK, MONTH;

        fun timeRange(clock: Clock): Pair<OffsetDateTime, OffsetDateTime> {
            val now = OffsetDateTime.now(clock).truncatedTo(ChronoUnit.DAYS)

            fun OffsetDateTime.shiftToStartOfDay(): OffsetDateTime {
                return OffsetDateTime.of(
                    /* year = */ year,
                    /* month = */ monthValue,
                    /* dayOfMonth = */ dayOfMonth,
                    /* hour = */ 0,
                    /* minute = */ 0,
                    /* second = */ 0,
                    /* nanoOfSecond = */ 0,
                    /* offset = */ ZoneOffset.UTC
                )
            }

            fun OffsetDateTime.shiftToEndOfDay(): OffsetDateTime {
                return OffsetDateTime.of(
                    /* year = */ year,
                    /* month = */ monthValue,
                    /* dayOfMonth = */ dayOfMonth,
                    /* hour = */ 23,
                    /* minute = */ 59,
                    /* second = */ 59,
                    /* nanoOfSecond = */ 0,
                    /* offset = */ ZoneOffset.UTC
                )
            }

            return when (this) {
                DAY -> {
                    now.shiftToStartOfDay() to now.shiftToEndOfDay()
                }

                WEEK -> {
                    val firstDayOfWeek = WeekFields.of(Locale.ENGLISH).firstDayOfWeek
                    val lastDayOfWeek = DayOfWeek.of(((firstDayOfWeek.value + 5) % DayOfWeek.values().size) + 1)

                    val startDate = now.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))
                    val endDate = now.with(TemporalAdjusters.nextOrSame(lastDayOfWeek))

                    startDate.shiftToStartOfDay() to endDate.shiftToEndOfDay()
                }

                MONTH -> {
                    val firstDayOfMonth = now.with(TemporalAdjusters.firstDayOfMonth())
                    val lastDayOfMonth = now.with(TemporalAdjusters.lastDayOfMonth())

                    firstDayOfMonth.shiftToStartOfDay() to lastDayOfMonth.shiftToEndOfDay()
                }
            }
        }
    }

    companion object {
        const val MIN_TIMES = 1

        val DEFAULT = FrequencyGoal(
            times = 1,
            period = Period.DAY
        )
    }
}
package com.enricog.entities

import kotlin.math.pow
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@JvmInline
value class Seconds private constructor(private val duration: Duration) : Comparable<Seconds> {

    val value: Long
        get() = duration.inWholeSeconds

    val minutes: Long
        get() = value / 60

    val secondsRemainingInMinute: Long
        get() = value - (minutes * 60)

    operator fun plus(other: Seconds): Seconds {
        return Seconds(duration + other.duration)
    }

    operator fun minus(other: Seconds): Seconds {
        return Seconds(duration - other.duration)
    }

    override operator fun compareTo(other: Seconds): Int {
        return duration.compareTo(other.duration)
    }

    override fun toString(): String {
        return "$minutes:$secondsRemainingInMinute"
    }

    companion object {
        private const val TIME_SEPARATOR = ":"
        private const val DEFAULT_TIME_VALUE = "0"

        fun from(value: Int): Seconds = Seconds(value.toDuration(DurationUnit.SECONDS))

        fun from(value: Long): Seconds = Seconds(value.toDuration(DurationUnit.SECONDS))

        fun from(value: String): Seconds {
            val values = value.split(TIME_SEPARATOR)

            fun extractValue(position: Int): Long {
                val stringValue = values
                    .getOrElse(position - 1) { DEFAULT_TIME_VALUE }
                    .takeIf { it.isNotBlank() }
                    ?: DEFAULT_TIME_VALUE
                return stringValue.toLong()
            }

            val length = values.size
            val seconds = (0..2).fold(0L) { accumulator, i ->
                accumulator + extractValue(length - i) * (60.0.pow(i)).toLong()
            }
            return Seconds(seconds.toDuration(DurationUnit.SECONDS))
        }
    }
}

val Int.seconds: Seconds
    get() = Seconds.from(this)

val Long.seconds: Seconds
    get() = Seconds.from(this)

val String.seconds: Seconds
    get() = Seconds.from(this)

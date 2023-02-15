package com.enricog.entities

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@JvmInline
value class Seconds private constructor(private val duration: Duration) : Comparable<Seconds> {

    val value: Long
        get() = duration.inWholeSeconds

    val inMinutes: Pair<Long, Long>
        get() = (value / 60) to (value - ((value / 60) * 60))

    operator fun plus(other: Seconds): Seconds {
        return Seconds(duration + other.duration)
    }

    operator fun minus(other: Seconds): Seconds {
        return Seconds(duration - other.duration)
    }

    operator fun times(scale: Int): Seconds {
        return Seconds(duration * scale)
    }

    override operator fun compareTo(other: Seconds): Int {
        return duration.compareTo(other.duration)
    }

    override fun toString(): String {
        return value.toString()
    }

    companion object {
        fun from(value: Int): Seconds = Seconds(value.toDuration(DurationUnit.SECONDS))

        fun from(value: Long): Seconds = Seconds(value.toDuration(DurationUnit.SECONDS))

        fun from(value: String): Seconds {
            return when {
                value.isBlank() -> from(0)

                else -> from(value.toLong())
            }
        }
    }
}

val Int.seconds: Seconds
    get() = Seconds.from(this)

val Long.seconds: Seconds
    get() = Seconds.from(this)

val String.seconds: Seconds
    get() = Seconds.from(this)

package com.enricog.entities

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

@JvmInline
value class Seconds private constructor(private val duration: Duration) {

    val value: Long
        get() = duration.inWholeSeconds

    companion object {
        fun from(value: Int): Seconds = Seconds(value.toDuration(DurationUnit.SECONDS))
        fun from(value: Long): Seconds = Seconds(value.toDuration(DurationUnit.SECONDS))
    }

    operator fun plus(other: Seconds): Seconds {
        return Seconds(duration + other.duration)
    }

    operator fun minus(other: Seconds): Seconds {
        return Seconds(duration - other.duration)
    }

    operator fun compareTo(other: Seconds): Int {
        return duration.compareTo(other.duration)
    }

    override fun toString(): String {
        return duration.toString()
    }

}

val Int.seconds: Seconds
    get() = Seconds.from(this)

val Long.seconds: Seconds
    get() = Seconds.from(this)

package com.enricog.ui.components.textField

import com.enricog.entities.Seconds
import com.enricog.entities.seconds
import kotlin.math.pow

/**
 * Type to define the visual representation of time inserted by the user
 * when is typing. This allow to not lose the "context" when typing.
 *
 * TimeText -> Seconds:
 *  - "9" -> 9 seconds
 *  - "89" -> 89 seconds
 *  - "891" (8:91) -> 571 seconds
 *
 * Seconds -> TimeText:
 *  - 9 seconds -> "9"
 *  - 89 seconds -> "129" (1:29)
 *  - 891 seconds -> "1531" (15:31)
 */
@JvmInline
value class TimeText private constructor(private val value: String) : Comparable<TimeText> {

    val length: Int get() = value.length

    fun toSeconds(): Seconds {
        val values = value.chunked(size = 2)

        fun extractValue(position: Int): Long {
            val stringValue = values
                .getOrElse(position - 1) { DEFAULT_TIME_VALUE }
                .takeIf { it.isNotBlank() }
                ?: DEFAULT_TIME_VALUE
            return stringValue.toLong()
        }

        return (0..2).fold(0L) { accumulator, i ->
            accumulator + extractValue(values.size - i) * (60.0.pow(i)).toLong()
        }.seconds
    }

    override fun compareTo(other: TimeText): Int {
        return value.compareTo(other.value)
    }

    override fun toString(): String {
        return value
    }

    companion object {
        private const val TIME_SEPARATOR = ":"
        private const val DEFAULT_TIME_VALUE = "0"

        fun from(value: Seconds): TimeText {
            val (m, s) = value.inMinutes
            val text = buildString {
                when {
                    m > 0L && s >= 0L -> {
                        append(m)
                        append(s.toString().padStart(length = 2, padChar = '0'))
                    }
                    m == 0L && s > 0L -> {
                        append(s)
                    }
                }
            }
            return TimeText(text)
        }

        fun from(value: String): TimeText {
            return TimeText(value.replace(oldValue = TIME_SEPARATOR, newValue = ""))
        }
    }
}

val Seconds.timeText: TimeText get() = TimeText.from(this)

val String.timeText: TimeText get() = TimeText.from(this)
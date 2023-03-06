package com.enricog.ui.components.textField

import androidx.compose.runtime.Immutable
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
@Immutable
@JvmInline
value class TimeText private constructor(private val value: String) : Comparable<TimeText> {

    val length: Int get() = value.length

    fun toSeconds(): Seconds {
        if (length > MAX_STRING_LENGTH) return MAX_VALUE_SECONDS

        val evenLength = when {
            length % 2 == 0 -> length
            else -> length + 1
        }
        val values = value
            .padStart(length = evenLength, padChar = '0')
            .chunked(size = 2)

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

    fun toPrettyString(): String {
        return buildString {
            val (m, s) = this@TimeText.toSeconds().inMinutes
            if (m > 0) {
                append("$m")
            }
            if (s > 0) {
                if (m > 0) {
                    append(":")
                }
                append("$s")
            }
        }
    }

    override fun toString(): String {
        return value
    }

    override fun compareTo(other: TimeText): Int {
        return value.compareTo(other.value)
    }

    companion object {
        private const val TIME_SEPARATOR = ":"
        private const val DEFAULT_TIME_VALUE = "0"

        private const val MAX_STRING_LENGTH = 6
        private val MAX_VALUE_SECONDS = 86400.seconds // 1h

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
            val sanitizedValue = value
                .replace(oldValue = TIME_SEPARATOR, newValue = "")
                .replace(oldValue = "\n", newValue = "")
                .replace(oldValue = "\r", newValue = "")
                .replace(oldValue = " ", newValue = "")
                .trimStart('0')
            return TimeText(sanitizedValue)
        }
    }
}

val Seconds.timeText: TimeText get() = TimeText.from(this)

val String.timeText: TimeText get() = TimeText.from(this)

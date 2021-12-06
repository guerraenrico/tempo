package com.enricog.entities

import java.lang.IllegalArgumentException
import kotlin.math.pow

@JvmInline
value class Rank private constructor(private val value: String) {

    init {
        require(value.isNotBlank()) { "Rank cannot be empty" }
        require(value.length == RANK_LENGTH) { "Rank value length should be $RANK_LENGTH got ${value.length}" }
    }

    val length: Int
        get() = value.length

    override fun toString(): String {
        return value
    }

    operator fun compareTo(other: Rank): Int {
        return value.compareTo(other.value)
    }

    companion object {
        private const val RANK_LENGTH = 4
        internal val MIN = Rank(value = "aaaa")
        internal val MAX = Rank(value = "zzzz")
        val NOT_SET = Rank(value = "----")

        private const val ALPHABET_SIZE = 26

        fun from(value: String): Rank {
            return Rank(value)
        }

        fun calculate(rank1: Rank, rank2: Rank): Rank {
            if (rank1 >= rank2) {
                throw IllegalArgumentException("$rank1 is higher or equal to $rank2")
            }

            val codePoints1 = rank1.value.codePoints().toArray()
            val codePoints2 = rank2.value.codePoints().toArray()

            var diff = 0.0
            for (i in 0 until RANK_LENGTH) {
                val firstCode = codePoints1[i]
                var secondCode = codePoints2[i]

                if (secondCode < firstCode) {
                    secondCode += ALPHABET_SIZE
                    codePoints2[i - 1] -= 1
                }

                val powRes = ALPHABET_SIZE.toDouble().pow(rank1.length.toDouble() - i - 1)
                diff += (secondCode - firstCode) * powRes
            }

            if (diff <= 1) {
                return from(rank1.value + Char('a'.code + ALPHABET_SIZE / 2).toString())
            }

            diff /= 2
            var offset = 0
            var newRankValue = ""
            for (i in 0 until RANK_LENGTH) {
                val diffInSymbols = (diff / ALPHABET_SIZE.toDouble().pow(i) % ALPHABET_SIZE).toInt()
                var elementCode =
                    rank1.value.codePointAt(rank2.length - i - 1) + diffInSymbols + offset
                offset = 0
                if (elementCode > 'z'.code) {
                    offset++
                    elementCode -= ALPHABET_SIZE
                }
                newRankValue += Char(elementCode)
            }
            return from(newRankValue.reversed())
        }
    }
}

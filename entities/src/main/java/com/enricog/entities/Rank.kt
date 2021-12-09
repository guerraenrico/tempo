package com.enricog.entities

import kotlin.math.pow

@JvmInline
value class Rank private constructor(private val value: String) : Comparable<Rank> {

    init {
        require(value.isNotBlank()) { "Rank cannot be empty" }
        require(value.length == RANK_LENGTH) { "Rank value length should be $RANK_LENGTH got ${value.length}" }
    }

    override fun toString(): String {
        return value
    }

    override operator fun compareTo(other: Rank): Int {
        return value.compareTo(other.value)
    }

    companion object {
        private const val RANK_LENGTH = 6
        private const val MIN_CHAR = 'a'
        private const val MAX_CHAR = 'z'
        private val MIN_RANK = Rank(value = "aaaaaa")
        private val MAX_RANK = Rank(value = "zzzzzz")

        private const val ALPHABET_SIZE = 26

        fun from(value: String): Rank {
            return Rank(value)
        }

        /**
         * Calculate the rank of the very first item of the list.
         */
        fun calculateFist(): Rank {
            return calculate(rank1 = MIN_RANK, rank2 = MAX_RANK)
        }

        /**
         * Calculate the rank for the fist item of the list.
         */
        fun calculateTop(rank: Rank): Rank {
            return calculate(rank1 = MIN_RANK, rank2 = rank)
        }

        /**
         * Calculate the rank for the last item of the list.
         */
        fun calculateBottom(rank: Rank): Rank {
            return calculate(rank1 = rank, rank2 = MAX_RANK)
        }

        /**
         * Calculate the rank between two other ranks
         */
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

                val powRes = ALPHABET_SIZE.toDouble().pow(RANK_LENGTH - i - 1)
                diff += (secondCode - firstCode) * powRes
            }

            if (diff <= 1) {
                return from(rank1.value + Char(MIN_CHAR.code + ALPHABET_SIZE / 2).toString())
            }

            diff /= 2

            val newRankValue = buildString {
                var offset = 0
                for (i in 0 until RANK_LENGTH) {
                    val diffInSymbols = (diff / ALPHABET_SIZE.toDouble().pow(i) % ALPHABET_SIZE)
                        .toInt()
                    var elementCode = rank1.value
                        .codePointAt(RANK_LENGTH - i - 1) + diffInSymbols + offset
                    offset = 0
                    if (elementCode > MAX_CHAR.code) {
                        offset++
                        elementCode -= ALPHABET_SIZE
                    }
                    append(Char(elementCode))
                }
            }

            return from(newRankValue.reversed())
        }
    }
}

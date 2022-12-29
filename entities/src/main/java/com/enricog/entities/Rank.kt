package com.enricog.entities

import kotlin.math.pow

/**
 * Lexorank based implementation simplified.
 * It is used to order items in the databased without changing all items
 * but only the item that has been moved.
 */
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

        fun calculate(rankTop: Rank?, rankBottom: Rank?): Rank {
            return when {
                rankTop == null && rankBottom != null -> calculateTop(rankBottom)
                rankTop != null && rankBottom == null -> calculateBottom(rankTop)
                rankTop != null && rankBottom != null -> calculate(rankTop, rankBottom)
                // else is same as rankTop == null && rankBottom == null
                else -> calculateFirst()
            }
        }

        /**
         * Calculate the rank of the very first item of the list.
         */
        fun calculateFirst(): Rank {
            return calculate(rankTop = MIN_RANK, rankBottom = MAX_RANK)
        }

        /**
         * Calculate the rank for the fist item of the list.
         */
        fun calculateTop(rank: Rank): Rank {
            return calculate(rankTop = MIN_RANK, rankBottom = rank)
        }

        /**
         * Calculate the rank for the last item of the list.
         */
        fun calculateBottom(rank: Rank): Rank {
            return calculate(rankTop = rank, rankBottom = MAX_RANK)
        }

        /**
         * Calculate the rank between two other ranks
         */
        fun calculate(rankTop: Rank, rankBottom: Rank): Rank {
            if (rankTop >= rankBottom) {
                throw IllegalArgumentException("RankTop($rankTop) is higher or equal to RankBottom($rankBottom)")
            }

            val codePoints1 = rankTop.value.codePoints().toArray()
            val codePoints2 = rankBottom.value.codePoints().toArray()

            var diff = 0.0
            for (i in RANK_LENGTH - 1 downTo 0) {
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
                return from(rankTop.value + Char(MIN_CHAR.code + ALPHABET_SIZE / 2).toString())
            }

            diff /= 2

            val newRankValue = buildString {
                var offset = 0
                for (i in 0 until RANK_LENGTH) {
                    val diffInSymbols = (diff / ALPHABET_SIZE.toDouble().pow(i) % ALPHABET_SIZE)
                        .toInt()
                    var elementCode = rankTop.value
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

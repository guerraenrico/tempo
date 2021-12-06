package com.enricog.entities

import java.lang.IllegalArgumentException
import org.junit.Assert.assertEquals
import org.junit.Test

class RankTest {

    @Test
    fun `calculate rank between min and max`() {
        val rank1 = Rank.MIN
        val rank2 = Rank.MAX
        val expected = Rank.from("mzzz")

        val actual = Rank.calculate(rank1 = rank1, rank2 = rank2)

        assertEquals(expected, actual)
    }

    @Test
    fun `calculate rank between`() {
        val rank1 = Rank.from("aaaa")
        val rank2 = Rank.from("cccc")
        val expected = Rank.from("bbbb")

        val actual = Rank.calculate(rank1 = rank1, rank2 = rank2)

        assertEquals(expected, actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `calculate rank should throw if rank1 is higher than rank2`() {
        val rank1 = Rank.from("cccc")
        val rank2 = Rank.from("aaaa")

        Rank.calculate(rank1 = rank1, rank2 = rank2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `calculate rank should throw if rank1 is equal to rank2`() {
        val rank1 = Rank.from("aaaa")
        val rank2 = Rank.from("aaaa")

        Rank.calculate(rank1 = rank1, rank2 = rank2)
    }
}

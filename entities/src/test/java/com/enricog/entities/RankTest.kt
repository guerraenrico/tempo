package com.enricog.entities

import java.lang.IllegalArgumentException
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RankTest {

    @Test
    fun `calculateFist should return middle rank`() {
        val expected = Rank.from("mzzzzz")

        val actual = Rank.calculateFist()

        assertEquals(expected, actual)
    }

    @Test
    fun `calculateTop should return the rank between the top most rank and the one indicated`() {
        val rank = Rank.from("cccccc")
        val expected = Rank.from("bbbbbb")

        val actual = Rank.calculateTop(rank = rank)

        assertEquals(expected, actual)
    }

    @Test
    fun `calculateBottom should return the rank between the bottom most rank and the one indicated`() {
        val rank = Rank.from("cccccc")
        val expected = Rank.from("obbbba")

        val actual = Rank.calculateBottom(rank = rank)

        assertEquals(expected, actual)
    }

    @Test
    fun `calculate should return a rank between two ranks`() {
        val rank1 = Rank.from("aaaaaa")
        val rank2 = Rank.from("cccccc")
        val expected = Rank.from("bbbbbb")

        val actual = Rank.calculate(rankTop = rank1, rankBottom = rank2)

        assertEquals(expected, actual)
    }

    @Test
    fun `calculate should return a rank between two ranks (not linear rank)`() {
        val rank1 = Rank.from("yjtmzz")
        val rank2 = Rank.from("zewtmz")
        val expected = Rank.from("yuidgm")

        val actual = Rank.calculate(rank1, rank2)

        assertEquals(expected, actual)
        assertTrue(rank1 < actual)
        assertTrue(rank2 > actual)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `calculate rank should throw if rank1 is higher than rank2`() {
        val rank1 = Rank.from("cccccc")
        val rank2 = Rank.from("aaaaaa")

        Rank.calculate(rankTop = rank1, rankBottom = rank2)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `calculate rank should throw if rank1 is equal to rank2`() {
        val rank1 = Rank.from("aaaaaa")
        val rank2 = Rank.from("aaaaaa")

        Rank.calculate(rankTop = rank1, rankBottom = rank2)
    }
}

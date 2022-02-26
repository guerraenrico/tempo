package com.enricog.base.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class CollectionsExtensionsKtTest {

    @Test
    fun `replace when selector matches`() {
        val list = listOf(1, 2, 3, 4)
        val expected = listOf(1, 7, 3, 4)

        val actual = list.replace(7) { it == 2 }

        assertEquals(expected, actual)
    }

    @Test
    fun `replace when selector does not match`() {
        val list = listOf(1, 2, 3, 4)

        val actual = list.replace(7) { it == 5 }

        assertEquals(list, actual)
    }
}

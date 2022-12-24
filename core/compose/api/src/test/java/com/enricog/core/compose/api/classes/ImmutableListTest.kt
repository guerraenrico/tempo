package com.enricog.core.compose.api.classes

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImmutableListTest {

    @Test
    fun `lists should be equal`() {
        val list1 = immutableListOf(1, 2)
        val list2 = immutableListOf(1, 2)

        assertTrue(list1 == list2)
    }

    @Test
    fun `lists should not be equal`() {
        val list1 = immutableListOf(1, 2)
        val list2 = immutableListOf(1, 3)

        assertFalse(list1 == list2)
    }
}
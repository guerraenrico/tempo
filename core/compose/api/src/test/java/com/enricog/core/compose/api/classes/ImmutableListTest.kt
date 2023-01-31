package com.enricog.core.compose.api.classes

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ImmutableListTest {

    @Test
    fun `lists should be equal`() {
        val list1 = immutableListOf(1, 2)
        val list2 = immutableListOf(1, 2)

        assertThat(list1).isEqualTo(list2)
    }

    @Test
    fun `lists should not be equal`() {
        val list1 = immutableListOf(1, 2)
        val list2 = immutableListOf(1, 3)

        assertThat(list1).isNotEqualTo(list2)
    }
}
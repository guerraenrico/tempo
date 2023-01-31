package com.enricog.base.extensions

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class CollectionsExtensionsKtTest {

    @Test
    fun `replace when selector matches`() {
        val list = listOf(1, 2, 3, 4)
        val expected = listOf(1, 7, 3, 4)

        val actual = list.replace(7) { it == 2 }

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `replace when selector does not match`() {
        val list = listOf(1, 2, 3, 4)
        val expected = listOf(1, 2, 3, 4)

        val actual = list.replace(7) { it == 5 }

        assertThat(actual).isEqualTo(expected)
    }
}

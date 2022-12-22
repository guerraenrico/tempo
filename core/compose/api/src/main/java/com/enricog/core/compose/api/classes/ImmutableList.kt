package com.enricog.core.compose.api.classes

import androidx.compose.runtime.Immutable

@Immutable
class ImmutableList<T : Any> internal constructor(private val list: List<T>) : List<T> by list {

    override fun equals(other: Any?): Boolean {
        return list == other
    }

    override fun hashCode(): Int {
        return list.hashCode()
    }
}

fun <T : Any> immutableListOf(vararg elements: T): ImmutableList<T> = ImmutableList(
    list = if (elements.isNotEmpty()) elements.asList() else emptyList()
)

fun <T : Any> emptyImmutableList(): ImmutableList<T> = ImmutableList(list = emptyList())

fun <T : Any> List<T>.asImmutableList() = ImmutableList(list = this)

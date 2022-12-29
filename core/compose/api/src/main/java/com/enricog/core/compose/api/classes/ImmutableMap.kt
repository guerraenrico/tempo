package com.enricog.core.compose.api.classes

import androidx.compose.runtime.Immutable

@Immutable
class ImmutableMap<K : Any, V : Any> internal constructor(private val map: Map<K, V>) :
    Map<K, V> by map {

    override fun equals(other: Any?): Boolean {
        return map == other
    }

    override fun hashCode(): Int {
        return map.hashCode()
    }

    override fun toString(): String {
        return map.toString()
    }
}

fun <K : Any, V : Any> immutableMapOf(vararg pairs: Pair<K, V>): ImmutableMap<K, V> = ImmutableMap(
    map = if (pairs.isNotEmpty()) mapOf(*pairs) else emptyMap()
)

fun <K : Any, V : Any> emptyImmutableMap(): ImmutableMap<K, V> = ImmutableMap(map = emptyMap())

fun <K : Any, V : Any> Map<K, V>.asImmutableMap() = ImmutableMap(map = this)

fun <K : Any, V : Any> Iterable<Pair<K, V>>.toImmutableMapMap(): ImmutableMap<K, V> = ImmutableMap(map = toMap())

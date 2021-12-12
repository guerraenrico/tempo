package com.enricog.core.extensions

inline fun <T> Iterable<T>.replace(value: T, predicate: (T) -> Boolean): List<T> {
    return map {
        when {
            predicate(it) -> value
            else -> it
        }
    }
}

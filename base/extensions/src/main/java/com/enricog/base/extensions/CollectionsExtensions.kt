package com.enricog.base.extensions

fun <T> Collection<T>.replace(value: T, predicate: (T) -> Boolean): List<T> {
    return map {
        when {
            predicate(it) -> value
            else -> it
        }
    }
}

fun <T, R> Collection<T>.mapToIfNotEmptyOrNull(transform: (Collection<T>) -> R): R? {
    return if (isEmpty()) null else transform(this)
}

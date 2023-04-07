package com.enricog.core.coroutines.async

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll as coroutinesAwaitAll

@Suppress("UNCHECKED_CAST")
suspend fun <T1, T2> awaitAll(deferred1: Deferred<T1>, deferred2: Deferred<T2>): Pair<T1, T2> {
    val results = coroutinesAwaitAll(deferred1, deferred2)
    return (results[0] as T1) to (results[1] as T2)
}

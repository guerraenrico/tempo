package com.enricog.core.coroutine.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val main: CoroutineDispatcher
    val cpu: CoroutineDispatcher
    val io: CoroutineDispatcher
}

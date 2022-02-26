package com.enricog.core.coroutines.dispatchers

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val main: CoroutineDispatcher
    val cpu: CoroutineDispatcher
    val io: CoroutineDispatcher
}

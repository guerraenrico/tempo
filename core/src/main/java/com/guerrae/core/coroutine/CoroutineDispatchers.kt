package com.guerrae.core.coroutine

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val main: CoroutineDispatcher
    val cpu: CoroutineDispatcher
    val io: CoroutineDispatcher
}
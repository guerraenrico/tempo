package com.enricog.base_test.coroutine

import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class TestCoroutineDispatchers : CoroutineDispatchers {
    override val main: CoroutineDispatcher = Dispatchers.Unconfined

    override val cpu: CoroutineDispatcher = Dispatchers.Unconfined

    override val io: CoroutineDispatcher = Dispatchers.Unconfined
}
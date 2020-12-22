package com.enricog.core.coroutine.dispatchers

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

internal class CoroutineDispatchersImpl @Inject constructor() : CoroutineDispatchers {

    override val main: CoroutineDispatcher = Dispatchers.Main

    override val cpu: CoroutineDispatcher = Dispatchers.Default

    override val io: CoroutineDispatcher = Dispatchers.IO
}
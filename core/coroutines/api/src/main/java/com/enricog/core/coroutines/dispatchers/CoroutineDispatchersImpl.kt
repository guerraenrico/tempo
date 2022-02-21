package com.enricog.core.coroutines.dispatchers

import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal class CoroutineDispatchersImpl @Inject constructor() : CoroutineDispatchers {

    override val main: CoroutineDispatcher = Dispatchers.Main

    override val cpu: CoroutineDispatcher = Dispatchers.Default

    override val io: CoroutineDispatcher = Dispatchers.IO
}

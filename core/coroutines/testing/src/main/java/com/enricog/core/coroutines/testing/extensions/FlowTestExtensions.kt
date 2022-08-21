package com.enricog.core.coroutines.testing.extensions

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import java.util.concurrent.atomic.AtomicInteger

/**
 * Test a flow eagerly: this function allow to test a flow differently from Turbine.
 * In Turbine the flow is collected with a channel, in which every item is being waited
 * before doing the assertion. This does not allow to have more time base specific tests since
 * it is making [advanceTimeBy] useless because the test is suspended until the child coroutines is executed.
 * With this method instead the assertion is executed eagerly without waiting for the child coroutine
 * to be executed, so it is required to use [advanceTimeBy] or [advanceUntilIdle] before doing the assertion
 * to make sure the the child coroutine is executed.
 * This has been made for very specific use cases to test the flow values in a specific point in time.
 * The test function from Turbine should be used in all other cases.
 */
suspend fun <T> Flow<T>.testEager(
    validate: suspend FlowTempo<T>.() -> Unit
) {
    coroutineScope {
        val items = mutableListOf<T>()
        val collectJob = launch {
            this@testEager.toList(items)
        }
        FlowTempoImpl(items, collectJob).apply {
            validate()
            cancel()
        }
    }
}

interface FlowTempo<T> {

    fun item(consume: Boolean = true): T

    fun skip(num: Int)

    suspend fun cancel()
}

private class FlowTempoImpl<T>(
    private val items: MutableList<T>,
    private val collectJob: Job
) : FlowTempo<T> {

    private var count = AtomicInteger(0)

    override fun item(consume: Boolean): T {
        return items.getOrNull(count.getAndIncrement())
            ?: throw AssertionError("Expected item but not found at position ${count.get()}")
    }

    override fun skip(num: Int) {
        count.updateAndGet { it + num }
    }

    override suspend fun cancel() {
        collectJob.cancelAndJoin()
    }
}

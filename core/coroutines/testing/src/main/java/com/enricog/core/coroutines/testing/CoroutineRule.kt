@file:OptIn(ExperimentalCoroutinesApi::class)

package com.enricog.core.coroutines.testing

import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

private val testScheduler = TestCoroutineScheduler()

class CoroutineRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(scheduler = testScheduler)
) : TestWatcher() {

    val dispatchers = object : CoroutineDispatchers {
        override val main: CoroutineDispatcher = testDispatcher
        override val cpu: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
    }

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    operator fun invoke(block: suspend TestScope.() -> Unit) {
        runTest {
            block()
        }
    }
}

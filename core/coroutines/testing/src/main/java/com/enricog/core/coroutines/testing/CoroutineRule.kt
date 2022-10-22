@file:OptIn(ExperimentalCoroutinesApi::class)

package com.enricog.core.coroutines.testing

import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class CoroutineRule(
    private val mainTestDispatcher: TestDispatcher = UnconfinedTestDispatcher()
) : TestWatcher() {

    fun getDispatchers(
        testDispatcher: TestDispatcher = UnconfinedTestDispatcher(mainTestDispatcher.scheduler)
    ) = object : CoroutineDispatchers {
        override val main: CoroutineDispatcher = testDispatcher
        override val cpu: CoroutineDispatcher = testDispatcher
        override val io: CoroutineDispatcher = testDispatcher
    }


    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(mainTestDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }

    operator fun invoke(block: suspend TestScope.() -> Unit) = runTest {
        block()
    }
}

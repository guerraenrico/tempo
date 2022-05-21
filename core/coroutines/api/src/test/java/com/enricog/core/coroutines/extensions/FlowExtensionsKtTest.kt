package com.enricog.core.coroutines.extensions

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalCoroutinesApi::class)
class FlowExtensionsKtTest {

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when maxSize is less or equal to 0`(): Unit = runTest {
        val flow = flow {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
        }

        flow.chunked(maxSize = 0, timeMillis = 100)
            .flowOn(UnconfinedTestDispatcher(testScheduler))
            .launchIn(this)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `should throw exception when timeMillis is less or equal to 0`(): Unit = runTest {
        val flow = flow {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
        }

        flow.chunked(maxSize = 10, timeMillis = 0)
            .flowOn(UnconfinedTestDispatcher(testScheduler))
            .launchIn(this)
    }

    @Test
    fun `should emit chunked values`() = runTest {
        val flow = flow {
            emit(1)
            emit(2)
            emit(3)
            emit(4)
        }

        flow.chunked(maxSize = 10, timeMillis = 100)
            .flowOn(UnconfinedTestDispatcher(testScheduler))
            .test {
                assertEquals(expected = listOf(1, 2, 3, 4), actual = awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun `should emit chunked values emitted before timeout`() = runTest {
        val flow = flow {
            emit(1)
            emit(2)
            delay(120)
            emit(3)
            emit(4)
        }

        flow.chunked(maxSize = 10, timeMillis = 100)
            .flowOn(UnconfinedTestDispatcher(testScheduler))
            .test {
                assertEquals(expected = listOf(1, 2), actual = awaitItem())
                assertEquals(expected = listOf(3, 4), actual = awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun `should emit chunked values without waiting timeout when reached max size`() = runTest {
        val flow = flow {
            emit(1)
            emit(2)
            delay(50)
            emit(3)
            emit(4)
            emit(5)
            delay(120)
            emit(6)
            emit(7)
            emit(8)
        }

        flow.chunked(maxSize = 4, timeMillis = 100)
            .flowOn(UnconfinedTestDispatcher(testScheduler))
            .test {
                assertEquals(expected = listOf(1, 2, 3, 4), actual = awaitItem())
                assertEquals(expected = listOf(5), actual = awaitItem())
                assertEquals(expected = listOf(6, 7, 8), actual = awaitItem())
                awaitComplete()
            }
    }

    @Test
    fun `test throttleFirst`() = runTest {
        val flow = (1..10).asFlow().onEach { delay(200) }

        flow
            .throttleFirst(timeMillis = 500)
            .flowOn(UnconfinedTestDispatcher(testScheduler))
            .test {
                assertEquals(expected = 1, actual = awaitItem())
                assertEquals(expected = 3, actual = awaitItem())
                assertEquals(expected = 5, actual = awaitItem())
                assertEquals(expected = 8, actual = awaitItem())
                assertEquals(expected = 10, actual = awaitItem())
                awaitComplete()
            }
    }
}
package com.enricog.base_android.viewmodel

import app.cash.turbine.test
import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.core.coroutine.dispatchers.CoroutineDispatchers
import kotlin.test.assertEquals
import org.junit.Rule
import org.junit.Test

class BaseViewModelTest {

    @get:Rule
    val coroutineRule = CoroutineRule()

    private sealed class TestState {
        object Idle : TestState()
        data class Data(val value: Int) : TestState()
    }

    private sealed class TestViewState {
        object Idle : TestViewState()
        data class Data(val value: String) : TestViewState()
    }

    private class TestStateConverter : StateConverter<TestState, TestViewState> {
        override suspend fun convert(state: TestState): TestViewState {
            return when (state) {
                TestState.Idle -> TestViewState.Idle
                is TestState.Data -> TestViewState.Data(state.value.toString())
            }
        }
    }

    private class TestViewModel(
        initialValue: TestState,
        converter: TestStateConverter,
        dispatchers: CoroutineDispatchers
    ) : BaseViewModel<TestState, TestViewState>(
        initialState = initialValue,
        converter = converter,
        dispatchers = dispatchers
    ) {
        fun setState(value: TestState) {
            state = value
        }

        fun runSetState(value: TestState) = runWhen<TestState.Data> {
            state = value
        }

        fun launchSetState(value: TestState) = launchWhen<TestState.Data> {
            state = value
        }
    }

    @Test
    fun `should set initial state on init`() = coroutineRule {
        buildState(initialValue = TestState.Idle).viewState.test {
            assertEquals(expected = TestViewState.Idle, actual = expectItem())
        }
    }

    @Test
    fun `should change state`() = coroutineRule {
        val sut = buildState(initialValue = TestState.Idle)
        sut.viewState.test {
            assertEquals(expected = TestViewState.Idle, actual = expectItem())

            sut.setState(value = TestState.Data(value = 1))

            assertEquals(expected = TestViewState.Data(value = "1"), actual = expectItem())
        }
    }

    @Test
    fun `should not emit state when setting the same state value`() = coroutineRule {
        val sut = buildState(initialValue = TestState.Idle)
        sut.viewState.test {
            assertEquals(expected = TestViewState.Idle, actual = expectItem())

            sut.setState(value = TestState.Idle)

            expectNoEvents()
        }
    }

    @Test
    fun `should execute runWhen block when current state is the one expected`() = coroutineRule {
        val sut = buildState(initialValue = TestState.Data(value = 1))
        sut.viewState.test {
            assertEquals(expected = TestViewState.Data(value = "1"), actual = expectItem())

            sut.runSetState(value = TestState.Data(value = 2))

            assertEquals(expected = TestViewState.Data(value = "2"), actual = expectItem())
        }
    }

    @Test
    fun `should not execute runWhen block when current state is not the one expected`() =
        coroutineRule {
            val sut = buildState(initialValue = TestState.Idle)
            sut.viewState.test {
                assertEquals(expected = TestViewState.Idle, actual = expectItem())

                sut.setState(value = TestState.Data(value = 1))

                expectNoEvents()
            }
        }

    @Test
    fun `should execute launchWhen block when current state is the one expected`() = coroutineRule {
        val sut = buildState(initialValue = TestState.Data(value = 1))
        sut.viewState.test {
            assertEquals(expected = TestViewState.Data(value = "1"), actual = expectItem())

            sut.launchSetState(value = TestState.Data(value = 2))

            assertEquals(expected = TestViewState.Data(value = "2"), actual = expectItem())
        }
    }

    @Test
    fun `should not execute launchWhen block when current state is not the one expected`() =
        coroutineRule {
            val sut = buildState(initialValue = TestState.Idle)
            sut.viewState.test {
                assertEquals(expected = TestViewState.Idle, actual = expectItem())

                sut.launchSetState(value = TestState.Data(value = 1))

                expectNoEvents()
            }
        }

    private fun buildState(initialValue: TestState): TestViewModel {
        val converter = TestStateConverter()
        return TestViewModel(initialValue, converter, coroutineRule.dispatchers)
    }
}

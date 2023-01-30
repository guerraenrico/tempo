package com.enricog.base.viewmodel

import app.cash.turbine.test
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.testing.CoroutineRule
import com.google.common.truth.Truth.assertThat
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
            updateState { value }
        }

        fun setStateWhen(value: TestState) {
            updateStateWhen<TestState.Data> { value }
        }

        fun runSetState(value: TestState) = runWhen<TestState.Data> {
            updateState { value }
        }

        fun launchSetState(value: TestState) = launchWhen<TestState.Data> {
            updateState { value }
        }
    }

    @Test
    fun `should set initial state on init`() = coroutineRule {
        buildViewModel(initialValue = TestState.Idle).viewState.test {
            assertThat(awaitItem()).isEqualTo(TestViewState.Idle)
        }
    }

    @Test
    fun `should change state`() = coroutineRule {
        val viewModel = buildViewModel(initialValue = TestState.Idle)
        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(TestViewState.Idle)

            viewModel.setState(value = TestState.Data(value = 1))

            assertThat(awaitItem()).isEqualTo(TestViewState.Data(value = "1"))
        }
    }

    @Test
    fun `should change state when current state type matches`() = coroutineRule {
        val viewModel = buildViewModel(initialValue = TestState.Data(value = 1))
        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(TestViewState.Data(value = "1"))

            viewModel.setStateWhen(value = TestState.Data(value = 2))

            assertThat(awaitItem()).isEqualTo(TestViewState.Data(value = "2"))
        }
    }

    @Test
    fun `should not emit state when setting the same state value`() = coroutineRule {
        val viewModel = buildViewModel(initialValue = TestState.Idle)
        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(TestViewState.Idle)

            viewModel.setState(value = TestState.Idle)

            expectNoEvents()
        }
    }

    @Test
    fun `should execute runWhen block when current state is the one expected`() = coroutineRule {
        val viewModel = buildViewModel(initialValue = TestState.Data(value = 1))
        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(TestViewState.Data(value = "1"))

            viewModel.runSetState(value = TestState.Data(value = 2))

            assertThat(awaitItem()).isEqualTo(TestViewState.Data(value = "2"))
        }
    }

    @Test
    fun `should not execute runWhen block when current state is not the one expected`() =
        coroutineRule {
            val viewModel = buildViewModel(initialValue = TestState.Idle)
            viewModel.viewState.test {
                assertThat(awaitItem()).isEqualTo(TestViewState.Idle)

                viewModel.runSetState(value = TestState.Data(value = 1))

                expectNoEvents()
            }
        }

    @Test
    fun `should execute launchWhen block when current state is the one expected`() = coroutineRule {
        val viewModel = buildViewModel(initialValue = TestState.Data(value = 1))
        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(TestViewState.Data(value = "1"))

            viewModel.launchSetState(value = TestState.Data(value = 2))

            assertThat(awaitItem()).isEqualTo(TestViewState.Data(value = "2"))
        }
    }

    @Test
    fun `should not execute launchWhen block when current state is not the one expected`() =
        coroutineRule {
            val viewModel = buildViewModel(initialValue = TestState.Idle)
            viewModel.viewState.test {
                assertThat(awaitItem()).isEqualTo(TestViewState.Idle)

                viewModel.launchSetState(value = TestState.Data(value = 1))

                expectNoEvents()
            }
        }

    private fun buildViewModel(initialValue: TestState): TestViewModel {
        val converter = TestStateConverter()
        return TestViewModel(
            initialValue = initialValue,
            converter = converter,
            dispatchers = coroutineRule.getDispatchers()
        )
    }
}

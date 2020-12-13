package com.enricog.timer

import app.cash.turbine.test
import com.enricog.base_test.coroutine.CoroutineRule
import com.enricog.timer.models.TimerViewState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlin.time.seconds

// TODO: Complete
class TimerViewModelTest {

//    @get:Rule
//    val coroutineRule = CoroutineRule()
//
//    private val converter: TimerStateConverter = TimerStateConverter()
//    private val reducer: TimerReducer = TimerReducer()
//
//    @Test
//    fun `should stop counting`() = coroutineRule {
//        val sut = buildSut(2)
//
//        advanceUntilIdle()
//        assertEquals(
//            TimerViewState.Counting(timeInSeconds = 1, isRunning = true),
//            sut.viewState.singleOrNull()
//        )
//
////        sut.onStartStopButtonClick()
//
////        advanceUntilIdle()
//
////        assertEquals(
////            TimerViewState.Counting(timeInSeconds = 1, isRunning = false),
////            sut.viewState.singleOrNull()
////        )
//    }
//
//    @Test
//    fun `should continue counting`() = coroutineRule {
//        val sut = buildSut(2)
//
//        advanceUntilIdle()
//        sut.viewState.test {
//            assertEquals(TimerViewState.Counting(timeInSeconds = 1, isRunning = true), expectItem())
//
//        }
//
//        sut.onStartStopButtonClick()
//
//        advanceUntilIdle()
//        sut.viewState.test {
//            assertEquals(
//                TimerViewState.Counting(timeInSeconds = 1, isRunning = false),
//                expectItem()
//            )
//        }
//
//        sut.onStartStopButtonClick()
//
//        advanceUntilIdle()
//        sut.viewState.test {
//            assertEquals(TimerViewState.Counting(timeInSeconds = 2, isRunning = true), expectItem())
//        }
//    }
//
//    @Test
//    fun `onRestart should stop and reset counting`() = coroutineRule {
//        val sut = buildSut(2)
//
//        advanceUntilIdle()
//        sut.viewState.test {
//            assertEquals(TimerViewState.Counting(timeInSeconds = 1, isRunning = true), expectItem())
//        }
//
//        advanceUntilIdle()
//        sut.viewState.test {
//            assertEquals(TimerViewState.Counting(timeInSeconds = 2, isRunning = true), expectItem())
//        }
//
//        sut.onRestartButtonClick()
//
//        advanceUntilIdle()
//        sut.viewState.test {
//            assertEquals(
//                TimerViewState.Counting(timeInSeconds = 0, isRunning = false),
//                expectItem()
//            )
//        }
//    }
//
//    private fun buildSut(timeInSeconds: Int): TimerViewModel {
//        return TimerViewModel(
//            dispatchers = coroutineRule.dispatchers,
//            converter = converter,
//            reducer = reducer
//        )
//    }
}
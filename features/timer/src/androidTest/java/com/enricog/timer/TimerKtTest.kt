package com.enricog.timer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Segment
import com.enricog.timer.models.*
import com.enricog.timer.ui_components.CountingSceneTestTag
import org.junit.Rule
import org.junit.Test

class TimerKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val timerActions = object : TimerActions {
        override fun onStartStopButtonClick() {}

        override fun onRestartSegmentButtonClick() {}
    }

    @Test
    fun shouldNotShowCountingScene() = composeRule {
        val viewState = TimerViewState.Idle

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(CountingSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldShowCountingScene() = composeRule {
        val viewState = TimerViewState.Counting(
            runningSegment = Segment.EMPTY,
            step = SegmentStep(
                count = Count(
                    timeInSeconds = 1,
                    isRunning = true,
                    isCompleted = false
                ), type = SegmentStepType.STARTING
            ),
            clockBackgroundColor = Color.Blue
        )

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(CountingSceneTestTag).assertIsDisplayed()
    }
}
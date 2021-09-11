package com.enricog.timer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.entities.seconds
import com.enricog.timer_service.service.Count
import com.enricog.timer_service.service.SegmentStep
import com.enricog.timer_service.service.SegmentStepType
import com.enricog.timer_service.models.TimerActions
import com.enricog.timer_service.service.TimerViewState
import com.enricog.timer.ui_components.CountingSceneTestTag
import org.junit.Rule
import org.junit.Test

class TimerScreenKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val timerActions = object : TimerActions {
        override fun onStartStopButtonClick() {}
        override fun onRestartSegmentButtonClick() {}
        override fun onResetButtonClick() {}
        override fun onDoneButtonClick() {}
        override fun onCloseButtonClick() {}
    }

    @Test
    fun shouldNotShowCountingScene() = composeRule {
        val viewState = com.enricog.timer_service.service.TimerViewState.Idle

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(CountingSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldShowCountingScene() = composeRule {
        val viewState = com.enricog.timer_service.service.TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = com.enricog.timer_service.service.SegmentStep(
                count = com.enricog.timer_service.service.Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = com.enricog.timer_service.service.SegmentStepType.STARTING
            ),
            clockBackgroundColor = Color.Blue,
            isRoutineCompleted = false
        )

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(CountingSceneTestTag).assertIsDisplayed()
    }
}

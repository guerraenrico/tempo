package com.enricog.features.timer

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.testing.invoke
import com.enricog.entities.seconds
import com.enricog.features.timer.models.Count
import com.enricog.features.timer.models.SegmentStep
import com.enricog.features.timer.models.SegmentStepType
import com.enricog.features.timer.models.TimerActions
import com.enricog.features.timer.models.TimerViewState
import com.enricog.features.timer.ui_components.CountingSceneTestTag
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
        val viewState = TimerViewState.Idle

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(CountingSceneTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldShowCountingScene() = composeRule {
        val viewState = TimerViewState.Counting(
            stepTitleId = R.string.title_segment_step_type_in_progress,
            segmentName = "segment name",
            step = SegmentStep(
                count = Count(
                    seconds = 1.seconds,
                    isRunning = true,
                    isCompleted = false
                ),
                type = SegmentStepType.STARTING
            ),
            clockBackgroundColor = Color.Blue,
            isRoutineCompleted = false
        )

        setContent { viewState.Compose(timerActions) }

        onNodeWithTag(CountingSceneTestTag).assertIsDisplayed()
    }
}

package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.Text
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.compose.testing.invoke
import com.enricog.core.compose.testing.text.assertInnerTextEquals
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo
import com.enricog.features.routines.ui_components.goal_label.GoalLabel
import com.enricog.features.routines.ui_components.time_type.TimeTypeStyle
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test

internal class RoutineSectionKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowAllElementInTheSection() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
            goalLabel = GoalLabel(
                stringResId = R.string.label_routine_goal_text_day,
                formatArgs = immutableListOf(1, 2)
            ),
            rounds = Text.String(value = "x2"),
            segmentsSummary = RoutineInfo.SegmentsSummary(
                estimatedTotalTime = "10".timeText,
                segmentTypesCount = immutableMapOf(
                    TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        backgroundColor = Color.Blue,
                        onBackgroundColor = Color.White,
                        id = "TIMER"
                    ) to 3,
                    TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_rest_name,
                        backgroundColor = Color.Red,
                        onBackgroundColor = Color.White,
                        id = "REST"
                    ) to 2,
                    TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_stopwatch_name,
                        backgroundColor = Color.DarkGray,
                        onBackgroundColor = Color.White,
                        id = "STOPWATCH"
                    ) to 1
                )
            )
        )

        setContent {
            TempoTheme {
                RoutineSection(routineInfo = routineInfo, onEditRoutine = {})
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineSectionTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionSummaryRoutineNameTestTag)
            .assertTextEquals("Routine Name")
        onNodeWithTag(testTag = RoutineSectionSummaryGoalTestTag)
            .assertTextEquals("Completed 1/2 today")
        onNodeWithTag(testTag = RoutineSectionSummaryRoundsTestTag, useUnmergedTree = true)
            .assertInnerTextEquals("x2")

        onNodeWithTag(testTag = RoutineSectionSummaryInfoTestTag, useUnmergedTree = true)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionEstimatedTotalTimeTestTag, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("00:10")
        onNodeWithTag(testTag = "RoutineSectionSegmentTypeCount_TIMER_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("3")
        onNodeWithTag(testTag = "RoutineSectionSegmentTypeCount_REST_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("2")
        onNodeWithTag(testTag = "RoutineSectionSegmentTypeCount_STOPWATCH_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("1")
    }

    @Test
    fun shouldNotShowSegmentSummaryWhenItIsNotSet() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
            goalLabel = GoalLabel(
                stringResId = R.string.label_routine_goal_text_day,
                formatArgs = immutableListOf(1, 2)
            ),
            rounds = Text.String(value = "x2"),
            segmentsSummary = null
        )

        setContent {
            TempoTheme {
                RoutineSection(routineInfo = routineInfo, onEditRoutine = {})
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineSectionTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionSummaryRoutineNameTestTag)
            .assertTextEquals("Routine Name")
        onNodeWithTag(testTag = RoutineSectionSummaryGoalTestTag)
            .assertTextEquals("Completed 1/2 today")
        onNodeWithTag(testTag = RoutineSectionSummaryRoundsTestTag, useUnmergedTree = true)
            .assertInnerTextEquals("x2")

        onNodeWithTag(testTag = RoutineSectionSummaryInfoTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun shouldNotShowEstimatedTotalTimeWhenNotSet() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
            goalLabel = GoalLabel(
                stringResId = R.string.label_routine_goal_text_day,
                formatArgs = immutableListOf(1, 2)
            ),
            rounds = Text.String(value = "x2"),
            segmentsSummary = RoutineInfo.SegmentsSummary(
                estimatedTotalTime = null,
                segmentTypesCount = immutableMapOf(
                    TimeTypeStyle(
                        nameStringResId = R.string.chip_time_type_stopwatch_name,
                        backgroundColor = Color.Blue,
                        onBackgroundColor = Color.White,
                        id = "STOPWATCH"
                    ) to 1
                )
            )
        )

        setContent {
            TempoTheme {
                RoutineSection(routineInfo = routineInfo, onEditRoutine = {})
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineSectionTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionSummaryRoutineNameTestTag)
            .assertTextEquals("Routine Name")
        onNodeWithTag(testTag = RoutineSectionSummaryGoalTestTag)
            .assertTextEquals("Completed 1/2 today")
        onNodeWithTag(testTag = RoutineSectionSummaryRoundsTestTag)
            .assertInnerTextEquals("x2")

        onNodeWithTag(testTag = RoutineSectionSummaryInfoTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionEstimatedTotalTimeTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = "RoutineSectionSegmentTypeCount_STOPWATCH_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("1")
    }

    @Test
    fun shouldNotShowGoalLabelWhenItIsNotSet() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
            goalLabel = null,
            segmentsSummary = null,
            rounds = Text.String(value = "x2"),
        )

        setContent {
            TempoTheme {
                RoutineSection(routineInfo = routineInfo, onEditRoutine = {})
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineSectionTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionSummaryGoalTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun shouldNotShowRoundslWhenItIsNotSet() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
            goalLabel = GoalLabel(
                stringResId = R.string.label_routine_goal_text_day,
                formatArgs = immutableListOf(1, 2)
            ),
            rounds = null,
            segmentsSummary = null
        )

        setContent {
            TempoTheme {
                RoutineSection(routineInfo = routineInfo, onEditRoutine = {})
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineSectionTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionSummaryRoundsTestTag)
            .assertDoesNotExist()
    }
}
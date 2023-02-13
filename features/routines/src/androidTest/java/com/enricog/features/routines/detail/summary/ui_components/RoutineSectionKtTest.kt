package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.compose.testing.invoke
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.TimeTypeColors
import org.junit.Rule
import org.junit.Test

internal class RoutineSectionKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowAllElementInTheSection() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
            segmentsSummary = RoutineInfo.SegmentsSummary(
                estimatedTotalTime = 10.seconds,
                segmentTypesCount = immutableMapOf(
                    TimeType(
                        nameStringResId = R.string.chip_time_type_timer_name,
                        color = TimeTypeColors.TIMER,
                        id = "TIMER"
                    ) to 3,
                    TimeType(
                        nameStringResId = R.string.chip_time_type_rest_name,
                        color = TimeTypeColors.REST,
                        id = "REST"
                    ) to 2,
                    TimeType(
                        nameStringResId = R.string.chip_time_type_stopwatch_name,
                        color = TimeTypeColors.STOPWATCH,
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
            .assertTextEquals("Routine Name")

        onNodeWithTag(testTag = RoutineSectionSummaryInfoTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionEstimatedTotalTimeTestTag, useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("10s")
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_TIMER_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("3")
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_REST_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("2")
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_STOPWATCH_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("1")
    }

    @Test
    fun shouldNotShowSegmentSummaryWhenItIsNotSet() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
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
            .assertTextEquals("Routine Name")

        onNodeWithTag(testTag = RoutineSectionSummaryInfoTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun shouldNotShowEstimatedTotalTimeWhenNotSet() = composeRule {
        val routineInfo = RoutineInfo(
            routineName = "Routine Name",
            segmentsSummary = RoutineInfo.SegmentsSummary(
                estimatedTotalTime = null,
                segmentTypesCount = immutableMapOf(
                    TimeType(
                        nameStringResId = R.string.chip_time_type_stopwatch_name,
                        color = TimeTypeColors.STOPWATCH,
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
            .assertTextEquals("Routine Name")

        onNodeWithTag(testTag = RoutineSectionSummaryInfoTestTag)
            .assertIsDisplayed()
        onNodeWithTag(testTag = RoutineSectionEstimatedTotalTimeTestTag, useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_STOPWATCH_TestTag", useUnmergedTree = true)
            .assertIsDisplayed()
            .assertTextEquals("1")
    }
}
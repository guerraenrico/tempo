package com.enricog.features.routines.list.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.compose.testing.invoke
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.features.routines.list.models.RoutinesItem
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test
import com.enricog.data.routines.api.entities.TimeType as TimeTypeEntity

class RoutineItemKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldRenderRoutineItemWithSegmentSummary() = composeRule {
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = RoutinesItem.RoutineItem.SegmentsSummary(
                totalTime = 12.seconds,
                segmentTypesCount = immutableMapOf(
                    TimeType.from(TimeTypeEntity.TIMER) to 2,
                    TimeType.from(TimeTypeEntity.REST) to 1,
                    TimeType.from(TimeTypeEntity.STOPWATCH) to 1
                )
            )
        )

        setContent {
            TempoTheme {
                RoutineItem(
                    routineItem = routineItem,
                    enableClick = true,
                    onClick = {},
                    onDelete = {},
                    onDuplicate = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineItemTestTag).assertIsDisplayed()
        onNodeWithText(text = routineItem.name).assertIsDisplayed()

        onNodeWithTag(testTag = RoutineItemCountTestTag, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithTag(testTag = RoutineItemTotalTimeTestTag, useUnmergedTree = true)
            .assertTextEquals("12s")
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_TIMER_TestTag", useUnmergedTree = true)
            .assertTextEquals("2")
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_REST_TestTag", useUnmergedTree = true)
            .assertTextEquals("1")
        onNodeWithTag(
            testTag = "RoutineItemSegmentTypeCount_STOPWATCH_TestTag",
            useUnmergedTree = true
        )
            .assertTextEquals("1")
    }

    @Test
    fun shouldRenderRoutineItemWithoutCountWhenSummaryIsNull() = composeRule {
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = null
        )

        setContent {
            TempoTheme {
                RoutineItem(
                    routineItem = routineItem,
                    enableClick = true,
                    onClick = {},
                    onDelete = {},
                    onDuplicate = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineItemTestTag).assertIsDisplayed()
        onNodeWithText(text = routineItem.name).assertIsDisplayed()

        onNodeWithTag(
            testTag = RoutineItemTotalTimeTestTag,
            useUnmergedTree = true
        ).assertDoesNotExist()
        onNodeWithTag(
            testTag = RoutineItemCountTestTag,
            useUnmergedTree = true
        ).assertDoesNotExist()
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_TIMER_TestTag", useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_REST_TestTag", useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(
            testTag = "RoutineItemSegmentTypeCount_STOPWATCH_TestTag",
            useUnmergedTree = true
        )
            .assertDoesNotExist()
    }

    @Test
    fun shouldRenderRoutineItemWithoutTotalTimeWhenIsNull() = composeRule {
        val routineItem = RoutinesItem.RoutineItem(
            id = 0.asID,
            name = "Routine",
            rank = "aaaaaa",
            segmentsSummary = RoutinesItem.RoutineItem.SegmentsSummary(
                totalTime = null,
                segmentTypesCount = immutableMapOf(
                    TimeType.from(TimeTypeEntity.STOPWATCH) to 1
                )
            )
        )

        setContent {
            TempoTheme {
                RoutineItem(
                    routineItem = routineItem,
                    enableClick = true,
                    onClick = {},
                    onDelete = {},
                    onDuplicate = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(testTag = RoutineItemTestTag).assertIsDisplayed()
        onNodeWithText(text = routineItem.name).assertIsDisplayed()

        onNodeWithTag(testTag = RoutineItemCountTestTag, useUnmergedTree = true).assertIsDisplayed()
        onNodeWithTag(
            testTag = RoutineItemTotalTimeTestTag,
            useUnmergedTree = true
        ).assertDoesNotExist()
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_TIMER_TestTag", useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(testTag = "RoutineItemSegmentTypeCount_REST_TestTag", useUnmergedTree = true)
            .assertDoesNotExist()
        onNodeWithTag(
            testTag = "RoutineItemSegmentTypeCount_STOPWATCH_TestTag",
            useUnmergedTree = true
        )
            .assertTextEquals("1")
    }
}
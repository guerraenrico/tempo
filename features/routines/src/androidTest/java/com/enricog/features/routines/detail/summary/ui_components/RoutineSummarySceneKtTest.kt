package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import com.enricog.core.compose.api.classes.immutableListOf
import com.enricog.core.compose.api.classes.immutableMapOf
import com.enricog.core.compose.testing.invoke
import com.enricog.entities.asID
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentSectionTitle
import com.enricog.features.routines.detail.ui.time_type.TimeType
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.TimeTypeColors
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

class RoutineSummarySceneKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldShowElementsWithoutSegmentHeaderWhenSegmentTitleIsVisible() = composeRule {
        val items = immutableListOf(
            RoutineInfo(
                routineName = "Routine Name",
                segmentsSummary = RoutineInfo.SegmentsSummary(
                    estimatedTotalTime = 10.seconds,
                    segmentTypesCount = immutableMapOf(
                        TimeType(
                            nameStringResId = R.string.chip_time_type_timer_name,
                            color = TimeTypeColors.TIMER,
                            id = "TIMER"
                        ) to 1
                    )
                )
            ),
            SegmentSectionTitle(error = null),
            baseSegmentItem
        )

        setContent {
            TempoTheme {
                RoutineSummaryScene(
                    summaryItems = items,
                    message = null,
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onSegmentDuplicate = {},
                    onRoutineStart = {},
                    onRoutineEdit = {},
                    onSegmentMoved = { _, _ -> },
                    onSnackbarEvent = {}
                )
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
            .assertTextEquals("1")

        onNodeWithTag(SegmentSectionTitleTestTag)
            .assertIsDisplayed()
        onNodeWithTag(SegmentItemTestTag)
            .assertIsDisplayed()
        onNodeWithTag(HeaderAddSegmentButtonTestTag)
            .assertDoesNotExist()
    }

    @Test
    fun shouldShowElementsWithSegmentHeaderWhenSegmentTitleIsNotVisible() = composeRule {
        val items = immutableListOf(
            RoutineInfo(
                routineName = "routineName",
                segmentsSummary = RoutineInfo.SegmentsSummary(
                    estimatedTotalTime = 110.seconds,
                    segmentTypesCount = immutableMapOf(
                        TimeType(
                            nameStringResId = R.string.chip_time_type_timer_name,
                            color = TimeTypeColors.TIMER,
                            id = "TIMER"
                        ) to 11
                    )
                )
            ),
            SegmentSectionTitle(error = null),
            baseSegmentItem.copy(id = 0.asID, name = "Segment 0"),
            baseSegmentItem.copy(id = 1.asID, name = "Segment 1"),
            baseSegmentItem.copy(id = 2.asID, name = "Segment 2"),
            baseSegmentItem.copy(id = 3.asID, name = "Segment 3"),
            baseSegmentItem.copy(id = 4.asID, name = "Segment 4"),
            baseSegmentItem.copy(id = 5.asID, name = "Segment 5"),
            baseSegmentItem.copy(id = 6.asID, name = "Segment 6"),
            baseSegmentItem.copy(id = 7.asID, name = "Segment 7"),
            baseSegmentItem.copy(id = 8.asID, name = "Segment 8"),
            baseSegmentItem.copy(id = 9.asID, name = "Segment 9"),
            baseSegmentItem.copy(id = 10.asID, name = "Segment 10")
        )

        setContent {
            TempoTheme {
                RoutineSummaryScene(
                    summaryItems = items,
                    message = null,
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onSegmentDuplicate = {},
                    onRoutineStart = {},
                    onRoutineEdit = {},
                    onSegmentMoved = { _, _ -> },
                    onSnackbarEvent = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutineSummaryColumnTestTag).performTouchInput {
            val edgeFuzzFactor = 0.083f
            val verticalEndFuzzed = (height * (1 - edgeFuzzFactor)).roundToInt().toFloat()
            val horizontalEndFuzzed = (width * (1 - edgeFuzzFactor)).roundToInt().toFloat()
            val start = Offset(horizontalEndFuzzed, verticalEndFuzzed)
            val end = Offset(horizontalEndFuzzed, top)
            swipe(start, end, 200)
        }

        waitForIdle()

        onNodeWithTag(RoutineSectionTestTag)
            .assertDoesNotExist()
        onNodeWithTag(SegmentSectionTitleTestTag)
            .assertDoesNotExist()
        onNodeWithTag(HeaderAddSegmentButtonTestTag)
            .assertIsDisplayed()
    }

    private val baseSegmentItem = SegmentItem(
        id = 1.asID,
        name = "Segment 1",
        time = 10.seconds,
        type = TimeType(
            nameStringResId = R.string.chip_time_type_timer_name,
            color = TimeTypeColors.TIMER,
            id = "TIMER"
        ),
        rank = "aaaaaa"
    )
}

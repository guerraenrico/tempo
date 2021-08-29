package com.enricog.routines.detail.summary.ui_components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.height
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipe
import androidx.compose.ui.test.top
import androidx.compose.ui.test.width
import com.enricog.base_test.compose.invoke
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.asID
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

class RoutineSummarySceneKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldRender_RoutineSection_SegmentSectionTitle_SegmentItems_notHeaderAddSegmentButton() = composeRule {
        val items = listOf(
            RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
            RoutineSummaryItem.SegmentSectionTitle(error = null),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY),
        )

        setContent {
            TempoTheme {
                RoutineSummaryScene(
                    summaryItems = items,
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onRoutineStart = {},
                    onRoutineEdit = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutineSectionTestTag).assertIsDisplayed()
        onNodeWithTag(SegmentSectionTitleTestTag).assertIsDisplayed()
        onNodeWithTag(SegmentItemTestTag).assertIsDisplayed()
        onNodeWithTag(HeaderAddSegmentButtonTestTag).assertDoesNotExist()
    }

    @Test
    fun shouldRender_HeaderAddSegmentButton_when_SegmentSectionTitle_isNotVisible() = composeRule {
        val items = listOf(
            RoutineSummaryItem.RoutineInfo(routineName = "routineName"),
            RoutineSummaryItem.SegmentSectionTitle(error = null),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 0.asID, name = "item0")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 1.asID, name = "item1")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 2.asID, name = "item2")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 3.asID, name = "item3")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 4.asID, name = "item4")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 5.asID, name = "item5")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 6.asID, name = "item6")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 7.asID, name = "item7")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 8.asID, name = "item8")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 9.asID, name = "item9")),
            RoutineSummaryItem.SegmentItem(segment = Segment.EMPTY.copy(id = 10.asID, name = "item10")),
        )

        setContent {
            TempoTheme {
                RoutineSummaryScene(
                    summaryItems = items,
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onRoutineStart = {},
                    onRoutineEdit = {}
                )
            }
        }

        waitForIdle()

        onNodeWithTag(RoutineSummaryColumnTestTag).performGesture {
            val edgeFuzzFactor = 0.083f
            val verticalEndFuzzed = (height * (1 - edgeFuzzFactor)).roundToInt().toFloat()
            val horizontalEndFuzzed = (width * (1 - edgeFuzzFactor)).roundToInt().toFloat()
            val start = Offset(horizontalEndFuzzed, verticalEndFuzzed)
            val end = Offset(horizontalEndFuzzed, top)
            swipe(start, end, 200)
        }

        waitForIdle()

        onNodeWithTag(RoutineSectionTestTag).assertDoesNotExist()
        onNodeWithTag(SegmentSectionTitleTestTag).assertDoesNotExist()
        onNodeWithTag(HeaderAddSegmentButtonTestTag).assertIsDisplayed()
    }
}

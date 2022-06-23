package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipe
import com.enricog.core.compose.testing.invoke
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.testing.entities.EMPTY
import com.enricog.entities.asID
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.RoutineInfo
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentItem
import com.enricog.features.routines.detail.summary.models.RoutineSummaryItem.SegmentSectionTitle
import com.enricog.ui.theme.TempoTheme
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

class RoutineSummarySceneKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldRender_RoutineSection_SegmentSectionTitle_SegmentItems_notHeaderAddSegmentButton() =
        composeRule {
            val items = listOf(
                RoutineInfo(routineName = "routineName"),
                SegmentSectionTitle(error = null),
                SegmentItem(segment = Segment.EMPTY),
            )

            setContent {
                TempoTheme {
                    RoutineSummaryScene(
                        summaryItems = items,
                        onSegmentAdd = {},
                        onSegmentSelected = {},
                        onSegmentDelete = {},
                        onRoutineStart = {},
                        onRoutineEdit = {},
                        onSegmentMoved = { _, _ -> }
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
            RoutineInfo(routineName = "routineName"),
            SegmentSectionTitle(error = null),
            SegmentItem(segment = Segment.EMPTY.copy(id = 0.asID, name = "item0")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 1.asID, name = "item1")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 2.asID, name = "item2")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 3.asID, name = "item3")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 4.asID, name = "item4")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 5.asID, name = "item5")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 6.asID, name = "item6")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 7.asID, name = "item7")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 8.asID, name = "item8")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 9.asID, name = "item9")),
            SegmentItem(segment = Segment.EMPTY.copy(id = 10.asID, name = "item10")),
        )

        setContent {
            TempoTheme {
                RoutineSummaryScene(
                    summaryItems = items,
                    onSegmentAdd = {},
                    onSegmentSelected = {},
                    onSegmentDelete = {},
                    onRoutineStart = {},
                    onRoutineEdit = {},
                    onSegmentMoved = { _, _ -> }
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

        onNodeWithTag(RoutineSectionTestTag).assertDoesNotExist()
        onNodeWithTag(SegmentSectionTitleTestTag).assertDoesNotExist()
        onNodeWithTag(HeaderAddSegmentButtonTestTag).assertIsDisplayed()
    }
}

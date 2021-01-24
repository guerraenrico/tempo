package com.enricog.routines.detail.summary.ui_components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.enricog.base_test.compose.invoke
import com.enricog.base_test.entities.routines.EMPTY
import com.enricog.entities.routines.Segment
import com.enricog.routines.detail.summary.models.RoutineSummaryItem
import com.enricog.ui_components.resources.TempoTheme
import org.junit.Rule
import org.junit.Test

class RoutineSummarySceneKtTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun shouldRender_RoutineSection_SegmentSectionTitle_SegmentItems() = composeRule {
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
    }

}
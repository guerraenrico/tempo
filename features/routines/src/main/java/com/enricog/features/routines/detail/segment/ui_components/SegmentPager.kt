package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.core.compose.api.modifiers.swipeable.SwipeableState
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.theme.TempoTheme
import kotlin.math.abs

@Composable
internal fun SegmentPager(
    swipeState: SwipeableState<TimeType>,
    timeText: TimeText,
    timeTypes: List<TimeType>,
    selectedType: TimeType,
    onSelectTimeTypeChange: (TimeType) -> Unit,
    onTimeTextChange: (TimeText) -> Unit,
    errors: Map<SegmentField, SegmentFieldError>,
    segmentTimeFieldIme: SegmentTimeFieldIme,
    modifier: Modifier = Modifier
) = BoxWithConstraints(modifier = modifier) {

    val circleRadius = maxWidth / 3
    val center = maxWidth / 2
    val circleSpace = 120.dp

    val tabSpace = TempoTheme.dimensions.spaceS
    val tabWidth = (maxWidth - (tabSpace * (timeTypes.size + 1))) / timeTypes.size
    val tabAnchors = timeTypes
        .mapIndexed { index, timeType -> (tabWidth.toPx() * index) + (tabSpace.toPx() * index) to timeType }
        .toMap()

    val tabDiff = abs(tabAnchors.keys.elementAt(0) - tabAnchors.keys.elementAt(1))
    val offset = ((center + circleSpace) * swipeState.offset.value / tabDiff).toPx() * -1
    val timeFieldAnchors = timeTypes
        .mapIndexed { index, timeType ->
            timeType to (center * (index + 1) + (circleSpace * index)).toPx() + offset
        }
        .toMap()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        SegmentTimeField(
            circleRadius = circleRadius,
            anchors = timeFieldAnchors,
            timeText = timeText,
            onTimeTextChange = onTimeTextChange,
            errors = errors,
            timeFieldIme = segmentTimeFieldIme
        )

        Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceS))

        SegmentTypeTabs(
            tabSpace = tabSpace,
            tabWidth = tabWidth,
            tabAnchors = tabAnchors,
            swipeState = swipeState,
            timeTypes = timeTypes,
            selectedTimeType = selectedType,
            onSelectTimeTypeChange = onSelectTimeTypeChange,
        )
    }
}
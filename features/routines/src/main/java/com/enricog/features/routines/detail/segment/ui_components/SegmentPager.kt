package com.enricog.features.routines.detail.segment.ui_components

import android.content.res.Configuration.ORIENTATION_PORTRAIT
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.core.compose.api.modifiers.swipeable.SwipeableState
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.ui.components.text.TempoText
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

    val configuration = LocalConfiguration.current

    val circleShrinkFactor = if (configuration.orientation == ORIENTATION_PORTRAIT) 3 else 6

    val circleRadius = maxWidth / circleShrinkFactor
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
            timeFieldIme = segmentTimeFieldIme
        )

        TempoText(
            modifier = Modifier
                .height(35.dp)
                .padding(vertical = TempoTheme.dimensions.spaceS)
                .align(alignment = Alignment.CenterHorizontally),
            text = stringResourceOrNull(errors[SegmentField.Time]?.stringResId) ?: "",
            style = TempoTheme.typography.caption.copy(
                color = TempoTheme.colors.error
            ),
            maxLines = 1
        )

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
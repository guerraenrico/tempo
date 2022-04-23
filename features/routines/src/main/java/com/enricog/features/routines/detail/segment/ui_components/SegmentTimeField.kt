package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.SwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.core.compose.api.extensions.toPx
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.Seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.ui.time_type.color
import com.enricog.ui.components.textField.TempoTimeField
import com.enricog.ui.theme.TempoTheme
import kotlin.math.abs

@Composable
internal fun SegmentTimeField(
    modifier: Modifier = Modifier,
    swipeState: SwipeableState<TimeType>,
    time: Seconds,
    timeTypes: List<TimeType>,
    errors: Map<SegmentField, Int>,
    onSegmentTimeChange: (Seconds) -> Unit,
) = BoxWithConstraints(modifier = modifier) {

    val keyboardController = LocalSoftwareKeyboardController.current

    val circleSize = maxWidth / 3
    val center = maxWidth / 2
    val circleSpace = 120.dp

    val tabSpace = TempoTheme.dimensions.spaceS
    val tabWidth = (maxWidth - (tabSpace * (timeTypes.size + 1))) / timeTypes.size
    val tabAnchors = timeTypes
        .mapIndexed { index, timeType -> timeType to (tabWidth.toPx() * index) + (tabSpace.toPx() * index) }
        .toMap()

    val tabDiff = abs(tabAnchors[TimeType.TIMER]!! - tabAnchors[TimeType.REST]!!)

    val offset = ((center + circleSpace) * swipeState.offset.value / tabDiff).toPx() * -1

    val anchors = timeTypes
        .mapIndexed { index, timeType ->
            timeType to (center * (index + 1) + (circleSpace * index)).toPx() + offset
        }
        .toMap()


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(circleSize * 2)
            .drawBehind {
                anchors.map { (timeType, offset) ->
                    drawCircle(
                        color = timeType.color(),
                        center = Offset(x = offset, y = circleSize.toPx()),
                        radius = circleSize.toPx()
                    )
                }
            }
    ) {
        TempoTimeField(
            seconds = time,
            onValueChange = onSegmentTimeChange,
            modifier = Modifier
                .padding(TempoTheme.dimensions.spaceM)
                .align(Alignment.Center),
            label = stringResource(R.string.field_label_segment_time),
            errorMessage = stringResourceOrNull(errors[SegmentField.TimeInSeconds]),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            )
        )
    }
}

//@Composable
//fun Something() {
//    Layout(content = {
//
//        Row() {
//
//        }
//
//    }) {
//
//    }
//}
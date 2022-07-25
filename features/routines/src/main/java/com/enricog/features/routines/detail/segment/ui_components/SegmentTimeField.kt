package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.ui.time_type.color
import com.enricog.ui.components.textField.TempoTimeField
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun SegmentTimeField(
    circleRadius: Dp,
    anchors: Map<TimeType, Float>,
    timeText: TimeText,
    onTimeTextChange: (TimeText) -> Unit,
    errors: Map<SegmentField, SegmentFieldError>,
    modifier: Modifier = Modifier
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height = circleRadius * 2)
            .drawBehind {
                anchors.map { (timeType, offset) ->
                    drawCircle(
                        color = timeType.color(),
                        center = Offset(x = offset, y = circleRadius.toPx()),
                        radius = circleRadius.toPx()
                    )
                }
            }
    ) {
        TempoTimeField(
            value = timeText,
            onValueChange = onTimeTextChange,
            modifier = Modifier
                .padding(TempoTheme.dimensions.spaceM)
                .align(Alignment.Center),
            errorText = stringResourceOrNull(errors[SegmentField.TimeInSeconds]?.stringResId),
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(
                onDone = { keyboardController?.hide() }
            ),
            showBackground = false,
            showIndicator = false,
            textStyle = TextStyle(
                fontSize = 73.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
        )
    }
}

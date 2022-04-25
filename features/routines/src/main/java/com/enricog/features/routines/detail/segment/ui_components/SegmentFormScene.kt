package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.rememberSwipeableState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.theme.TempoTheme

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
internal fun SegmentFormScene(
    segment: SegmentFields,
    errors: Map<SegmentField, SegmentFieldError>,
    timeTypes: List<TimeType>,
    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (TimeText) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit
) {
    val swipeState = rememberSwipeableState(initialValue = segment.type) {
        onSegmentTimeTypeChange(it)
        true
    }
    val draggableState = rememberDraggableState {
        swipeState.performDrag(it)
    }

    Column(
        modifier = Modifier
            .testTag(SegmentFormSceneTestTag)
            .fillMaxSize()
            .background(TempoTheme.colors.background)
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                reverseDirection = true,
                onDragStopped = { swipeState.performFling(it) }
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState(0))
        ) {

            SegmentTypeTabs(
                modifier = Modifier.fillMaxWidth(),
                state = swipeState,
                timeTypes = timeTypes,
                selected = segment.type,
                onSelectChange = onSegmentTimeTypeChange
            )

//            TempoTextField(
//                value = segment.name,
//                onValueChange = onSegmentNameChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(TempoTheme.dimensions.spaceM)
//                    .focusRequester(segmentNameRef),
//                label = stringResource(R.string.field_label_segment_name),
//                errorMessage = stringResourceOrNull(id = errors[SegmentField.Name]),
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
//                keyboardActions = KeyboardActions(
//                    onNext = { segmentTimeRef.requestFocus() }
//                )
//            )

            SegmentTimeField(
                modifier = Modifier.fillMaxWidth(),
                swipeState = swipeState,
                timeTypes = timeTypes,
                time = segment.time,
                errors = errors,
                onTimeChange = onSegmentTimeChange
            )


            // TODO hide/disable time field if type selected is stopwatch
//            TempoTimeField(
//                seconds = segment.time,
//                onValueChange = onSegmentTimeChange,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(TempoTheme.dimensions.spaceM)
//                    .focusRequester(segmentTimeRef),
//                label = stringResource(R.string.field_label_segment_time),
//                errorMessage = stringResourceOrNull(errors[SegmentField.TimeInSeconds]),
//                imeAction = ImeAction.Done,
//                keyboardActions = KeyboardActions(
//                    onDone = { keyboardController?.hide() }
//                )
//            )
        }

        TempoButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(TempoTheme.dimensions.spaceM),
            onClick = onSegmentConfirmed,
            color = TempoButtonColor.Confirm,
            text = stringResource(R.string.button_save),
            contentDescription = stringResource(R.string.content_description_button_save_segment)
        )
    }
}



package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.core.compose.api.modifiers.horizontalListItemSpacing
import com.enricog.data.routines.api.entities.Segment
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.entities.Seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.ui_components.TimeTypeChip
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.textField.TempoTextField
import com.enricog.ui.components.textField.TempoTimeField
import com.enricog.ui.theme.TempoTheme

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class)
@Composable
internal fun SegmentFormScene(
    segment: Segment,
    errors: Map<SegmentField, Int>,
    timeTypes: List<TimeType>,
    onSegmentNameChange: (String) -> Unit,
    onSegmentTimeChange: (Seconds) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit
) = BoxWithConstraints {
    val keyboardController = LocalSoftwareKeyboardController.current
    val (segmentNameRef, segmentTimeRef) = remember { FocusRequester.createRefs() }

    val width = constraints.maxWidth.toFloat()
    val swipeState = rememberSwipeableState(initialValue = segment.type) {
        onSegmentTimeTypeChange(it)
        true
    }
    val anchors =
        timeTypes.mapIndexed { index, timeType -> width / (index + 1) to timeType }.toMap()

    Column(
        modifier = Modifier
            .testTag(SegmentFormSceneTestTag)
            .fillMaxSize()
            .background(TempoTheme.colors.background)
            .swipeable(
                state = swipeState,
                anchors = anchors,
                thresholds = { _, _ -> FractionalThreshold(0.5f) },
                orientation = Orientation.Horizontal
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState(0))
        ) {
            SelectableTimeType(
                timeTypes = timeTypes,
                selected = segment.type,
                onSelectChange = onSegmentTimeTypeChange
            )

            TempoTextField(
                value = segment.name,
                onValueChange = onSegmentNameChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TempoTheme.dimensions.spaceM)
                    .focusRequester(segmentNameRef),
                label = stringResource(R.string.field_label_segment_name),
                errorMessage = stringResourceOrNull(id = errors[SegmentField.Name]),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { segmentTimeRef.requestFocus() }
                )
            )

            // TODO hide/disable time field if type selected is stopwatch
            TempoTimeField(
                seconds = segment.time,
                onValueChange = onSegmentTimeChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TempoTheme.dimensions.spaceM)
                    .focusRequester(segmentTimeRef),
                label = stringResource(R.string.field_label_segment_time),
                errorMessage = stringResourceOrNull(errors[SegmentField.TimeInSeconds]),
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                )
            )
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

@Composable
private fun SelectableTimeType(
    timeTypes: List<TimeType>,
    selected: TimeType,
    onSelectChange: (TimeType) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(TempoTheme.dimensions.spaceS)
            .fillMaxWidth()
    ) {
        timeTypes.mapIndexed { index, timeType ->
            TimeTypeChip(
                value = timeType,
                isSelected = selected == timeType,
                onClick = onSelectChange,
                modifier = Modifier
                    .horizontalListItemSpacing(
                        itemPosition = index,
                        spacing = TempoTheme.dimensions.spaceS,
                        includeEdge = true
                    )
                    .weight(weight = 1f, fill = true)
            )
        }
    }
}

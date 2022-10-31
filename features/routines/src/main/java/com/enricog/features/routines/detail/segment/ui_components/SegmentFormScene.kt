package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.core.compose.api.modifiers.swipeable.rememberSwipeableState
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentFieldError
import com.enricog.features.routines.detail.segment.models.SegmentFields
import com.enricog.features.routines.detail.segment.models.SegmentViewState.Data.Message
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.components.textField.TempoTextField
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.theme.TempoTheme

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
internal fun SegmentFormScene(
    segment: SegmentFields,
    errors: Map<SegmentField, SegmentFieldError>,
    timeTypes: List<TimeType>,
    message: Message?,
    onSegmentNameChange: (TextFieldValue) -> Unit,
    onSegmentTimeChange: (TimeText) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val (segmentNameRef, segmentTimeRef) = remember { FocusRequester.createRefs() }

    val snackbarHostState = rememberSnackbarHostState()
    val swipeState = rememberSwipeableState(initialValue = segment.type) {
        onSegmentTimeTypeChange(it)
        true
    }
    val draggableState = rememberDraggableState {
        swipeState.drag(it)
    }

    if (message != null) {
        val messageText = stringResource(id = message.textResId)
        LaunchedEffect(snackbarHostState) {
            val event = snackbarHostState.show(message = messageText, actionText = null)
            onSnackbarEvent(event)
        }
    }

    TempoSnackbarHost(
        state = snackbarHostState,
        content = {
            Box(
                modifier = Modifier
                    .testTag(SegmentFormSceneTestTag)
                    .fillMaxSize()
                    .background(TempoTheme.colors.background)
                    .draggable(
                        state = draggableState,
                        orientation = Orientation.Horizontal,
                        reverseDirection = true,
                        onDragStopped = { swipeState.fling(it) }
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState(0))
                ) {
                    TempoTextField(
                        value = segment.name,
                        onValueChange = onSegmentNameChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(TempoTheme.dimensions.spaceM)
                            .focusRequester(segmentNameRef),
                        labelText = stringResource(R.string.field_label_segment_name),
                        errorText = stringResourceOrNull(id = errors[SegmentField.Name]?.stringResId),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(
                            onNext = { segmentTimeRef.requestFocus() }
                        )
                    )

                    SegmentPager(
                        modifier = Modifier.fillMaxWidth(),
                        swipeState = swipeState,
                        timeText = segment.time,
                        timeTypes = timeTypes,
                        selectedType = segment.type,
                        onSelectTimeTypeChange = onSegmentTimeTypeChange,
                        onTimeTextChange = onSegmentTimeChange,
                        errors = errors,
                        segmentTimeFieldIme = SegmentTimeFieldIme(
                            action = ImeAction.Done,
                            keyboardActions = KeyboardActions(
                                onDone = { keyboardController?.hide() }
                            ),
                            focusRequester = segmentTimeRef
                        )
                    )
                }
            }
        },
        anchor = {
            TempoButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(TempoTheme.dimensions.spaceM),
                onClick = onSegmentConfirmed,
                color = TempoButtonColor.Accent,
                text = stringResource(R.string.button_save),
                contentDescription = stringResource(R.string.content_description_button_save_segment)
            )
        }
    )
}



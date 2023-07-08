package com.enricog.features.routines.detail.segment.ui_components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentInputs
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.ui.time_type.TimeTypeStyle
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.components.textField.TempoTextField
import com.enricog.ui.components.textField.TempoTimeField
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.theme.TempoTheme

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
internal fun SegmentFormScene(
    state: SegmentViewState.Data,
    inputs: SegmentInputs,
    onSegmentNameChange: (TextFieldValue) -> Unit,
    onSegmentRoundsChange: (TextFieldValue) -> Unit,
    onSegmentTimeChange: (TimeText) -> Unit,
    onSegmentTimeTypeChange: (TimeTypeStyle) -> Unit,
    onSegmentConfirmed: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val (segmentNameRef, segmentRoundsRef, segmentTimeRef) = remember { FocusRequester.createRefs() }

    val snackbarHostState = rememberSnackbarHostState()

    if (state.message != null) {
        val messageText = stringResource(id = state.message.textResId)
        val actionText = stringResource(id = state.message.actionTextResId)
        LaunchedEffect(snackbarHostState) {
            val event = snackbarHostState.show(message = messageText, actionText = actionText)
            onSnackbarEvent(event)
        }
    }

    TempoSnackbarHost(
        modifier = Modifier.testTag(SegmentFormSceneTestTag),
        state = snackbarHostState,
        content = {
            SegmentPager(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = TempoTheme.dimensions.spaceM,
                        start = TempoTheme.dimensions.spaceM,
                        end = TempoTheme.dimensions.spaceM,
                        bottom = 85.dp
                    ),
                timeTypeStyles = state.timeTypeStyles,
                selectedType = state.selectedTimeTypeStyle,
                onSelectTimeTypeChange = onSegmentTimeTypeChange
            ) {
                TempoTextField(
                    value = inputs.name,
                    onValueChange = onSegmentNameChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = TempoTheme.dimensions.spaceXL)
                        .focusRequester(segmentNameRef),
                    labelText = stringResource(R.string.field_label_segment_name),
                    errorText = state.errors[SegmentField.Name]?.let {
                        stringResource(id = it.stringResId, formatArgs = it.formatArgs)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { segmentRoundsRef.requestFocus() }
                    )
                )

                TempoTextField(
                    value = inputs.rounds,
                    onValueChange = onSegmentRoundsChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = TempoTheme.dimensions.spaceXL)
                        .focusRequester(segmentRoundsRef),
                    labelText = stringResource(R.string.field_label_segment_rounds),
                    errorText = state.errors[SegmentField.Rounds]?.let {
                        stringResource(id = it.stringResId, formatArgs = it.formatArgs)
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (state.isTimeFieldVisible) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            if (state.isTimeFieldVisible) {
                                segmentTimeRef.requestFocus()
                            } else {
                                keyboardController?.hide()
                            }
                        }
                    )
                )

                AnimatedVisibility(
                    visible = state.isTimeFieldVisible,
                    enter = fadeIn() + slideInHorizontally(),
                    exit = fadeOut() + slideOutHorizontally(),
                ) {
                    TempoTimeField(
                        value = inputs.time,
                        onValueChange = onSegmentTimeChange,
                        modifier = Modifier
                            .focusRequester(segmentTimeRef),
                        labelText = stringResource(R.string.field_label_segment_time),
                        errorText = state.errors[SegmentField.Time]?.let {
                            stringResource(id = it.stringResId, formatArgs = it.formatArgs)
                        },
                        supportingText = stringResource(R.string.field_support_text_segment_time),
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(
                            onDone = { keyboardController?.hide() }
                        ),
                        textStyle = TextStyle(
                            fontSize = 50.sp,
                            fontWeight = FontWeight.Bold
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
                iconContentDescription = stringResource(R.string.content_description_button_save_segment)
            )
        }
    )
}



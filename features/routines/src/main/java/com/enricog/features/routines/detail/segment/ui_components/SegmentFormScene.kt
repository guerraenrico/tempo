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
import com.enricog.core.compose.api.extensions.stringResourceOrNull
import com.enricog.entities.seconds
import com.enricog.features.routines.R
import com.enricog.features.routines.detail.segment.models.SegmentField
import com.enricog.features.routines.detail.segment.models.SegmentViewState
import com.enricog.features.routines.detail.ui.time_type.TimeType
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
    onSegmentNameChange: (TextFieldValue) -> Unit,
    onSegmentTimeChange: (TimeText) -> Unit,
    onSegmentTimeTypeChange: (TimeType) -> Unit,
    onSegmentConfirmed: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val (segmentNameRef, segmentTimeRef) = remember { FocusRequester.createRefs() }

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
                    .padding(TempoTheme.dimensions.spaceM)
                    .fillMaxSize()
                    .padding(bottom = 85.dp),
                timeTypes = state.timeTypes,
                selectedType = state.segment.type,
                onSelectTimeTypeChange = onSegmentTimeTypeChange
            ) {
                TempoTextField(
                    value = state.segment.name,
                    onValueChange = onSegmentNameChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = TempoTheme.dimensions.spaceXL)
                        .focusRequester(segmentNameRef),
                    labelText = stringResource(R.string.field_label_segment_name),
                    errorText = stringResourceOrNull(id = state.errors[SegmentField.Name]?.stringResId),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(
                        onNext = { segmentTimeRef.requestFocus() }
                    )
                )

                AnimatedVisibility(
                    visible = state.segment.time != null,
                    enter = fadeIn() + slideInHorizontally(),
                    exit = fadeOut() + slideOutHorizontally(),
                ) {
                    TempoTimeField(
                        value = state.segment.time ?: TimeText.from(0.seconds),
                        onValueChange = onSegmentTimeChange,
                        modifier = Modifier
                            .focusRequester(segmentTimeRef),
                        labelText = stringResource(R.string.field_label_segment_time),
                        errorText = stringResourceOrNull(state.errors[SegmentField.Time]?.stringResId),
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
                contentDescription = stringResource(R.string.content_description_button_save_segment)
            )
        }
    )
}



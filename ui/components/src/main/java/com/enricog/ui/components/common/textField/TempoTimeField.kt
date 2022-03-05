package com.enricog.ui.components.common.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enricog.entities.Seconds
import com.enricog.entities.seconds
import com.enricog.ui.components.R
import com.enricog.ui.theme.TempoTheme

private val NUMERIC_REGEX = Regex("^[0-9]+\$|^\$|^\\s\$")
private val FIElD_WIDTH = 70.dp
private val TEXT_STYLE = tempoTextFieldBaseStyle.copy(textAlign = TextAlign.Center)
private val MAX_TIME_SECONDS = 3600.seconds

@Composable
fun TempoTimeField(
    seconds: Seconds,
    onValueChange: (Seconds) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val (minRef, secsRef) = remember { FocusRequester.createRefs() }

    fun getFormattedTextFieldValue(value: Long): TextFieldValue {
        val format = "%02d"
        val text = String.format(format, value)
        return TextFieldValue(text = text, selection = TextRange(text.length))
    }

    fun onTextFieldChang(
        minutesTextFieldValue: TextFieldValue,
        secondsTextFieldValue: TextFieldValue
    ) {
        if (
            minutesTextFieldValue.text.matches(NUMERIC_REGEX) &&
            secondsTextFieldValue.text.matches(NUMERIC_REGEX)
        ) {
            val value = "${minutesTextFieldValue.text}:${secondsTextFieldValue.text}".seconds
            if (value <= MAX_TIME_SECONDS) {
                onValueChange(value)
            }
        }
    }

    val textFieldMinuteValue = remember(seconds) {
        getFormattedTextFieldValue(seconds.minutes)
    }
    val textFieldSecondsValue = remember(seconds) {
        getFormattedTextFieldValue(seconds.secondsRemainingInMinute)
    }
    val textFieldMinutesChangeCallback = { value: TextFieldValue ->
        onTextFieldChang(value, textFieldSecondsValue)
    }
    val textFieldSecondsChangeCallback = { value: TextFieldValue ->
        onTextFieldChang(textFieldMinuteValue, value)
        if (value.text == "0" && seconds.secondsRemainingInMinute == 0L) {
            minRef.requestFocus()
        }
    }
    Column(
        modifier = modifier
    ) {
        if (label != null) {
            Text(
                modifier = Modifier.padding(bottom = TempoTheme.dimensions.spaceS),
                text = label,
                style = TempoTheme.typography.caption,
                maxLines = 1,
                color = TempoTheme.colors.onSurface
            )
        }
        Row {
            TempoTextFieldBase(
                value = textFieldMinuteValue,
                onValueChange = textFieldMinutesChangeCallback,
                modifier = Modifier
                    .width(FIElD_WIDTH)
                    .focusRequester(minRef),
                textStyle = TEXT_STYLE,
                label = stringResource(id = R.string.label_minutes),
                leadingIcon = null,
                trailingIcon = null,
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { secsRef.requestFocus() }),
                singleLine = true,
                maxLines = 1,
                visualTransformation = VisualTransformation.None,
                shape = TempoTheme.shapes.small.copy(
                    bottomEnd = ZeroCornerSize,
                    bottomStart = ZeroCornerSize,
                    topEnd = ZeroCornerSize
                )
            )
            TempoTextFieldBase(
                value = textFieldSecondsValue,
                onValueChange = textFieldSecondsChangeCallback,
                modifier = Modifier
                    .width(FIElD_WIDTH)
                    .focusRequester(secsRef),
                textStyle = TEXT_STYLE,
                label = stringResource(id = R.string.label_seconds),
                leadingIcon = null,
                trailingIcon = null,
                errorMessage = null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                singleLine = true,
                maxLines = 1,
                visualTransformation = VisualTransformation.None,
                shape = TempoTheme.shapes.small.copy(
                    bottomEnd = ZeroCornerSize,
                    bottomStart = ZeroCornerSize,
                    topStart = ZeroCornerSize
                )
            )
        }
        if (errorMessage != null) {
            TempoTextFieldBaseErrorText(errorMessage)
        }
    }
}

@Preview
@Composable
private fun Preview() {
    TempoTimeField(
        seconds = 250.seconds,
        onValueChange = {},
    )
}

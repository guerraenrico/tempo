package com.enricog.ui_components.common.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
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
import com.enricog.ui_components.resources.TempoTheme

private val numericRegex = Regex("^[0-9]+\$|^\$|^\\s\$")
private val fieldWidth = 70.dp
private val textStyle = tempoTextFieldBaseStyle.copy(textAlign = TextAlign.Center)

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
    fun getFormattedTextFieldValue(value: Long): TextFieldValue {
        val format = "%02d"
        val text = String.format(format, value)
        return TextFieldValue(text = text, selection = TextRange(text.length))
    }

    val (minRef, secsRef) = remember { FocusRequester.createRefs() }

    val textFieldMinuteValue = remember(seconds) {
        val minutes = seconds.value / 60
        getFormattedTextFieldValue(minutes)
    }
    val textFieldSecondsValue = remember(seconds) {
        val minutes = seconds.value / 60
        val secs = seconds.value - (minutes * 60)
        getFormattedTextFieldValue(secs)
    }
    val textFieldMinutesChangeCallback = { value: TextFieldValue ->
        if (numericRegex.matches(value.text)) {
            onValueChange("${value.text}:${textFieldSecondsValue.text}".seconds)
        }
    }
    val textFieldSecondsChangeCallback = { value: TextFieldValue ->
        if (numericRegex.matches(value.text)) {
            onValueChange("${textFieldMinuteValue.text}:${value.text}".seconds)
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
                    .width(fieldWidth)
                    .focusRequester(minRef),
                textStyle = textStyle,
                label = null,
                leadingIcon = null,
                trailingIcon = null,
                errorMessage = errorMessage,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(onNext = { secsRef.requestFocus() }),
                singleLine = true,
                maxLines = 1,
                visualTransformation = VisualTransformation.None
            )
            TempoTextFieldBase(
                value = textFieldSecondsValue,
                onValueChange = textFieldSecondsChangeCallback,
                modifier = Modifier
                    .width(fieldWidth)
                    .focusRequester(secsRef),
                textStyle = textStyle,
                label = null,
                leadingIcon = null,
                trailingIcon = null,
                errorMessage = errorMessage,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                singleLine = true,
                maxLines = 1,
                visualTransformation = VisualTransformation.None
            )
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

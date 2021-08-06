package com.enricog.ui_components.common.textField

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enricog.entities.Seconds
import com.enricog.entities.seconds

private val numericRegex = Regex("^\\d*:?\\d+\$")

@Composable
fun TempoTimeField(
    seconds: Seconds,
    onValueChange: (Seconds) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val textFieldMinuteValue = remember(seconds) {
        val minutes = seconds.value / 60
        val format = "%02d"
        val text = String.format(format, minutes)
        TextFieldValue(text = text, selection = TextRange(text.length))
    }
    val textFieldSecondsValue = remember(seconds) {
        val minutes = seconds.value / 60
        val secs = seconds.value - (minutes * 60)
        val format = "%02d"
        val text = String.format(format, secs)
        TextFieldValue(text = text, selection = TextRange(text.length))
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
    Row(
        modifier = modifier
    ) {
        TempoTextFieldBase(
            value = textFieldMinuteValue,
            onValueChange = textFieldMinutesChangeCallback,
            modifier = Modifier
                .width(100.dp),
            textStyle = tempoTextFieldBaseStyle,
            label = label,
            leadingIcon = null,
            trailingIcon = null,
            errorMessage = errorMessage,
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
            keyboardActions = keyboardActions,
            singleLine = true,
            maxLines = 1,
            visualTransformation = VisualTransformation.None
        )
        TempoTextFieldBase(
            value = textFieldSecondsValue,
            onValueChange = textFieldSecondsChangeCallback,
            modifier = Modifier
                .width(100.dp),
            textStyle = tempoTextFieldBaseStyle,
            label = label,
            leadingIcon = null,
            trailingIcon = null,
            errorMessage = errorMessage,
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
            keyboardActions = keyboardActions,
            singleLine = true,
            maxLines = 1,
            visualTransformation = VisualTransformation.None
        )
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

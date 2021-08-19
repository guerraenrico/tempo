package com.enricog.ui_components.common.textField

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TempoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = tempoTextFieldBaseStyle,
    label: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
) {
    var textFieldValueState by remember { mutableStateOf(TextFieldValue(text = value)) }
    val textFieldValue = textFieldValueState.copy(text = value)

    val textFieldValueChangeCallback = { textValue: TextFieldValue ->
        textFieldValueState = textValue
        if (textValue.text != value) {
            onValueChange(textValue.text)
        }
    }
    TempoTextFieldBase(
        value = textFieldValue,
        onValueChange = textFieldValueChangeCallback,
        modifier = modifier,
        textStyle = textStyle,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        errorMessage = errorMessage,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        singleLine = singleLine,
        maxLines = maxLines
    )
}

@Preview
@Composable
private fun Preview() {
    TempoTextField(
        value = "something",
        onValueChange = {}
    )
}

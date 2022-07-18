package com.enricog.ui.components.textField

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TempoTextField(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
) {
    require(errorText == null || errorText.isNotBlank()) { "Error text cannot be blank" }
    require(labelText == null || labelText.isNotBlank()) { "Label text cannot be blank" }
    require(supportingText == null || supportingText.isNotBlank()) { "Supporting text cannot be blank" }

    TempoTextFieldBase(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        labelText = labelText,
        supportingText = supportingText,
        errorText = errorText,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
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
        value = TextFieldValue(text = "text"),
        onValueChange = {}
    )
}

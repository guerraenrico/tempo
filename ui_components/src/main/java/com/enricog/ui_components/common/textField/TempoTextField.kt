package com.enricog.ui_components.common.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AmbientTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import com.enricog.ui_components.resources.dimensions

@Composable
fun TempoTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AmbientTextStyle.current,
    label: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = false,
    maxLines: Int = Int.MAX_VALUE,
) {
    TempoTextFieldBase(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = textStyle,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isErrorValue = isErrorValue,
        errorMessage = errorMessage,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        maxLines = maxLines
    )
}

@Composable
fun TempoNumberField(
    value: Long,
    onValueChange: (Long) -> Unit,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = AmbientTextStyle.current,
    label: String? = null,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isErrorValue: Boolean = false,
    errorMessage: String? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    maxLines: Int = 1,
) {
    TempoTextFieldBase(
        value = value.toString(),
        onValueChange = { onValueChange(it.toLong()) },
        modifier = modifier,
        textStyle = textStyle,
        label = label,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        isErrorValue = isErrorValue,
        errorMessage = errorMessage,
        visualTransformation = visualTransformation,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        singleLine = singleLine,
        maxLines = maxLines
    )
}

@Composable
private fun TempoTextFieldBase(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    textStyle: TextStyle,
    label: String?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    isErrorValue: Boolean,
    errorMessage: String?,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    singleLine: Boolean,
    maxLines: Int,
) {
    val labelText: @Composable (() -> Unit)? = if (label != null) {
        @Composable { Text(label) }
    } else null

    Column {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier,
            textStyle = textStyle,
            label = labelText,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isErrorValue = isErrorValue,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            singleLine = singleLine,
            maxLines = maxLines
        )
        if (errorMessage != null) {
            Text(
                modifier = Modifier.padding(top = MaterialTheme.dimensions.spaceS),
                text = "errorMessage",
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                color = MaterialTheme.colors.error
            )
        }
    }
}

package com.enricog.ui.components.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview

private val NUMERIC_REGEX = Regex("^[0-9]+\$|^\$|^\\s\$")

@Composable
fun TempoTimeField(
    value: TimeText,
    onValueChange: (TimeText) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    require(errorMessage == null || errorMessage.isNotBlank()) { "Error message cannot be blank" }
    require(label == null || label.isNotBlank()) { "Label cannot be blank" }

    val textFieldValue = TextFieldValue(
        text = value.toString(),
        selection = TextRange(value.length)
    )
    val textFieldSecondsChangeCallback = { newValue: TextFieldValue ->
        if (newValue.text.matches(NUMERIC_REGEX)) {
            onValueChange(newValue.text.timeText)
        }
    }

    TempoTextFieldBase(
        value = textFieldValue,
        onValueChange = textFieldSecondsChangeCallback,
        modifier = modifier.fillMaxWidth(),
        label = label,
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
        visualTransformation = TimeVisualTransformation
    )
}

private object TimeVisualTransformation : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val transformedText = getFormattedTextFieldValue(text)
        return TransformedText(
            text = transformedText,
            offsetMapping = object : OffsetMapping {
                override fun originalToTransformed(offset: Int): Int {
                    return transformedText.length
                }

                override fun transformedToOriginal(offset: Int): Int {
                    return text.length
                }
            }
        )
    }

    fun getFormattedTextFieldValue(text: AnnotatedString): AnnotatedString {
        val formattedText = text.text
            .padStart(length = 4, padChar = '0')
            .chunked(size = 2)
            .joinToString(":")
        return AnnotatedString(text = formattedText)
    }
}

@Preview
@Composable
private fun Preview() {
    TempoTimeField(
        value = "420".timeText,
        onValueChange = {},
    )
}

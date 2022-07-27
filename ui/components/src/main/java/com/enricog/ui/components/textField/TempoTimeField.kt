package com.enricog.ui.components.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.enricog.ui.theme.TempoTheme

private val NUMERIC_REGEX = Regex("^[0-9]+\$|^\$|^\\s\$")

@Composable
fun TempoTimeField(
    value: TimeText,
    onValueChange: (TimeText) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    supportingText: String? = null,
    errorText: String? = null,
    textStyle: TextStyle = TempoTheme.typography.textField,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    showBackground: Boolean = true,
    showIndicator: Boolean = true
) {
    require(errorText == null || errorText.isNotBlank()) { "Error text cannot be blank" }
    require(labelText == null || labelText.isNotBlank()) { "Label text cannot be blank" }
    require(supportingText == null || supportingText.isNotBlank()) { "Supporting text cannot be blank" }

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
        labelText = labelText,
        supportingText = supportingText,
        errorText = errorText,
        leadingIcon = null,
        trailingIcon = null,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1,
        visualTransformation = TimeVisualTransformation,
        textStyle = textStyle,
        showBackground = showBackground,
        showIndicator = showIndicator
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

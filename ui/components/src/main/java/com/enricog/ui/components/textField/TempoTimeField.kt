package com.enricog.ui.components.textField

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.white
import kotlin.math.min

private val NUMERIC_REGEX = Regex("^[0-9]+\$|^\$|^\\s\$")
private val TEXT_STYLE = tempoTextFieldBaseStyle.copy(textAlign = TextAlign.Center)
private val TEXT_EMPTY_STYLE = SpanStyle(
    fontWeight = FontWeight.Normal,
    color = white,
    fontSize = 20.sp
)

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
        textStyle = TEXT_STYLE,
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
        visualTransformation = TimeVisualTransformation,
        shape = TempoTheme.shapes.small.copy(
            bottomEnd = ZeroCornerSize,
            bottomStart = ZeroCornerSize,
            topStart = ZeroCornerSize
        )
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
        val formattedText = text.text.formatted()

        val spanStyles = buildList {
            val to = text.text.length.let { textLength ->
                val t = when {
                    textLength > 2 -> formattedText.length - textLength - 1
                    else -> formattedText.length - textLength
                }
                min(formattedText.length, t)
            }
            add(AnnotatedString.Range(item = TEXT_EMPTY_STYLE, start = 0, end = to))
        }


        return AnnotatedString(text = formattedText, spanStyles = spanStyles)
    }
}

private fun String.formatted(): String {
    return padStart(length = 4, padChar = '0')
        .chunked(size = 2)
        .joinToString(":")
}

@Preview
@Composable
private fun Preview() {
    TempoTimeField(
        value = "420".timeText,
        onValueChange = {},
    )
}

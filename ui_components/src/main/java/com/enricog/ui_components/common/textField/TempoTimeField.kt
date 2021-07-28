package com.enricog.ui_components.common.textField

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import com.enricog.entities.Seconds
import com.enricog.entities.seconds

private val numericRegex = Regex("^[0-9]+\$|^\$|^\\s\$")

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
    val textFieldValue = remember(seconds) {
        val text = seconds.value.toString()
        TextFieldValue(text = text, selection = TextRange(text.length))
    }
    val textFieldValueChangeCallback = { value: TextFieldValue ->
        if (numericRegex.matches(value.text)) {
            onValueChange(value.text.seconds)
        }
    }
    TempoTextFieldBase(
        value = textFieldValue,
        onValueChange = textFieldValueChangeCallback,
        visualTransformation = { text ->
            val textTime = buildTimeText(text.text.replace(":", "").toLong())
            TransformedText(
                textTime,
                object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = textTime.length
                    override fun transformedToOriginal(offset: Int): Int = textTime.length
                }
            )
        },
        modifier = modifier,
        textStyle = tempoTextFieldBaseStyle,
        label = label,
        leadingIcon = null,
        trailingIcon = null,
        errorMessage = errorMessage,
        keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
        keyboardActions = keyboardActions,
        singleLine = true,
        maxLines = 1
    )
}

private fun buildTimeText(timeInSeconds: Long): AnnotatedString {
    val minutes = timeInSeconds / 100
    val seconds = timeInSeconds - (minutes * 100)
    val spannableStyle = tempoTextFieldBaseStyle.toSpanStyle()

    val timeBuilder = StringBuilder()
    val spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>()

    val format = "%02d"
    val minutesString = String.format(format, minutes)
    var to = minutesString.length
    spanStyles.add(AnnotatedString.Range(spannableStyle, 0, to))
    timeBuilder.append(minutesString)

    var from = to
    to += 1
    spanStyles.add(AnnotatedString.Range(spannableStyle, from, to))
    timeBuilder.append(":")
    from += 1

    val secondsString = String.format(format, seconds)
    to += secondsString.length
    timeBuilder.append(secondsString)
    spanStyles.add(AnnotatedString.Range(spannableStyle, from, to))

    return AnnotatedString(text = timeBuilder.toString(), spanStyles = spanStyles)
}

@Preview
@Composable
private fun Preview() {
    TempoTimeField(
        seconds = 250.seconds,
        onValueChange = {},
    )
}

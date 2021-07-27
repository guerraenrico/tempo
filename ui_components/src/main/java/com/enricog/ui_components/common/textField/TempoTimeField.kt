package com.enricog.ui_components.common.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.enricog.entities.Seconds
import com.enricog.entities.seconds
import com.enricog.ui_components.resources.FontFamilyDefault
import com.enricog.ui_components.resources.TempoTheme
import com.enricog.ui_components.resources.white

private val textFieldStyle: TextStyle = TextStyle(
    fontFamily = FontFamilyDefault,
    fontWeight = FontWeight.Bold,
    color = white,
    fontSize = 26.sp
)

@Preview
@Composable
private fun TempoTimeFieldPreview() {
    TempoTimeField(
        seconds = 250.seconds,
        onValueChange = {},
    )
}

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
    val composableLabel: @Composable (() -> Unit)? = label?.let { @Composable { Text(label) } }
    val textFieldValue = remember(seconds) {
        val text = seconds.value.toString()
        TextFieldValue(text, selection = TextRange(text.length))
    }
    val textFieldValueToSeconds = { value: TextFieldValue ->
        if (numericRegex.matches(value.text)) {
            onValueChange(value.text.seconds)
        }
    }
    Column(
        modifier = modifier
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = textFieldValueToSeconds,
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
            modifier = Modifier.fillMaxWidth(),
            textStyle = textFieldStyle,
            label = composableLabel,
            isError = errorMessage != null,
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
            keyboardActions = keyboardActions,
            singleLine = true,
            maxLines = 1
        )
        if (errorMessage != null) {
            Text(
                modifier = Modifier.padding(top = TempoTheme.dimensions.spaceS),
                text = errorMessage,
                style = TempoTheme.typography.caption,
                maxLines = 1,
                color = TempoTheme.colors.error
            )
        }
    }
}

private fun buildTimeText(timeInSeconds: Long): AnnotatedString {
    val minutes = timeInSeconds / 100
    val seconds = timeInSeconds - (minutes * 100)
    val spannableStyle = textFieldStyle.toSpanStyle()

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

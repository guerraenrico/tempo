package com.enricog.ui_components.common.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.enricog.entities.Seconds
import com.enricog.entities.seconds
import com.enricog.ui_components.resources.FontFamilyDefault
import com.enricog.ui_components.resources.dimensions
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

@Composable
fun TempoTimeField(
    seconds: Seconds,
    onValueChange: (Seconds) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    val composableLabel: @Composable (() -> Unit)? = label?.let { @Composable { Text(label) } }
    val textFieldValue = TextFieldValue(buildTimeText(seconds.value))
    val textFieldValueToSeconds = { value: TextFieldValue ->
        onValueChange(value.text.toLong().seconds)
    }
    Column(
        modifier = modifier
    ) {
        TextField(
            value = textFieldValue,
            onValueChange = textFieldValueToSeconds,
            modifier = Modifier.fillMaxWidth(),
            textStyle = textFieldStyle,
            label = composableLabel,
            isError = errorMessage != null,
            keyboardOptions = keyboardOptions.copy(keyboardType = KeyboardType.Number),
            singleLine = true,
            maxLines = 1
        )
        if (errorMessage != null) {
            Text(
                modifier = Modifier.padding(top = MaterialTheme.dimensions.spaceS),
                text = errorMessage,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                color = MaterialTheme.colors.error
            )
        }
    }
}

private fun buildTimeText(timeInSeconds: Long): AnnotatedString {
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds - (minutes * 60)
    val spannableStyle = textFieldStyle.toSpanStyle()

    val timeBuilder = StringBuilder()
    var format = "%01d"
    val spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    var from = 0
    var to = 0

    if (minutes > 0) {
        format = "%02d"
        val minutesString = String.format(format, minutes)
        to = minutesString.length
        spanStyles.add(AnnotatedString.Range(spannableStyle, 0, to))
        timeBuilder.append(minutesString)

        from = to
        to += 1
        spanStyles.add(AnnotatedString.Range(spannableStyle, from, to))
        timeBuilder.append(":")
        from += 1
    }
    val secondsString = String.format(format, seconds)
    to += secondsString.length
    timeBuilder.append(secondsString)
    spanStyles.add(AnnotatedString.Range(spannableStyle, from, to))

    return AnnotatedString(text = timeBuilder.toString(), spanStyles = spanStyles)
}

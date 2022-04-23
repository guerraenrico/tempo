package com.enricog.ui.components.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
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
import com.enricog.entities.Seconds
import com.enricog.entities.seconds
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.white
import kotlin.math.min

private val NUMERIC_REGEX = Regex("^[0-9]+\$|^\$|^\\s\$")
private val TEXT_STYLE = tempoTextFieldBaseStyle.copy(textAlign = TextAlign.Center)
private val MAX_TIME_SECONDS = 3600.seconds
private const val MAX_LENGTH = 4

@Composable
fun TempoTimeField(
    seconds: Seconds,
    onValueChange: (Seconds) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    errorMessage: String? = null,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {

    var textFieldValueState by remember {
        mutableStateOf(TextFieldValue(text = seconds.toText()))
    }

    val textFieldSecondsChangeCallback = { value: TextFieldValue ->
        if (value.text.matches(NUMERIC_REGEX) && value.text.length <= MAX_LENGTH) {
            val formattedValue = value.text.formatted().seconds
            if (formattedValue <= MAX_TIME_SECONDS) {
                textFieldValueState = value
                onValueChange(formattedValue)
            }
        }
    }
    Column(
        modifier = modifier
    ) {
        if (label != null) {
            Text(
                modifier = Modifier.padding(bottom = TempoTheme.dimensions.spaceS),
                text = label,
                style = TempoTheme.typography.caption,
                maxLines = 1,
                color = TempoTheme.colors.onSurface
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            TempoTextFieldBase(
                value = textFieldValueState,
                onValueChange = textFieldSecondsChangeCallback,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                textStyle = TEXT_STYLE,
                label = null,
                leadingIcon = null,
                trailingIcon = null,
                errorMessage = null,
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
        if (errorMessage != null) {
            TempoTextFieldBaseErrorText(errorMessage)
        }
    }
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
            add(AnnotatedString.Range(item = EmptyStyle, start = 0, end = to))
        }


        return AnnotatedString(text = formattedText, spanStyles = spanStyles)
    }
}

private fun String.formatted(): String {
    return padStart(length = 4, padChar = '0')
        .chunked(size = 2)
        .joinToString(":")
}

private fun Seconds.toText(): String {
    val (m, s) = inMinutes
    return buildString {
        if (m > 0) {
            append(m)
        }
        append(s)
    }
}

private val EmptyStyle = SpanStyle(
    fontWeight = FontWeight.Normal,
    color = white,
    fontSize = 20.sp
)

@Preview
@Composable
private fun Preview() {
    TempoTimeField(
        seconds = 250.seconds,
        onValueChange = {},
    )
}

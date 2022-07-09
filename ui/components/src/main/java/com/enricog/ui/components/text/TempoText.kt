package com.enricog.ui.components.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import com.enricog.ui.theme.LocalTempoTextFieldStyle

@Composable
fun TempoText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = LocalTempoTextFieldStyle.current,
    letterSpacing: TextUnit = TextUnit.Unspecified,
) {
    TempoText(
        text = AnnotatedString(text),
        modifier = modifier,
        textAlign = textAlign,
        style = style,
        letterSpacing = letterSpacing
    )
}

@Composable
fun TempoText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = LocalTempoTextFieldStyle.current,
    letterSpacing: TextUnit = TextUnit.Unspecified,
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = style,
        letterSpacing = letterSpacing
    )
}
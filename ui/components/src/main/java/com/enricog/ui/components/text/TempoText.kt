package com.enricog.ui.components.text

import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TempoText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    style: TextStyle = LocalTextStyle.current
) {
    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        style = style
    )
}
package com.guerrae.timer.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.SpanStyleRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift.Companion.Superscript
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.resources.white

@Composable
internal fun Clock(backgroundColor: Color, timeInSeconds: Int) {
    val shape = CircleShape
    Column(
        modifier = Modifier
            .shadow(elevation = 20.dp, shape = shape)
            .background(color = backgroundColor, shape = shape)
            .height(200.dp)
            .width(200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimeText(timeInSeconds = timeInSeconds)
    }
}

@Composable
private fun TimeText(timeInSeconds: Int) {
    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds - (minutes * 60)

    val timeBuilder = StringBuilder()
    val spanStyles = mutableListOf<SpanStyleRange>()
    var from = 0

    if (minutes >= 0) {
        from = minutes.toString().length
        spanStyles.add(SpanStyleRange(NumberStyle, 0, from))
        timeBuilder.append(minutes)

        spanStyles.add(SpanStyleRange(SeparatorStyle, from++, from))
        timeBuilder.append(":")
    }
    timeBuilder.append(seconds)
    spanStyles.add(SpanStyleRange(NumberStyle, from, from + seconds.toString().length))

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = AnnotatedString(
                text = timeBuilder.toString(),
                spanStyles = spanStyles,
            ),
            textAlign = TextAlign.Center,
        )
    }
}

private val NumberStyle = SpanStyle(
    color = white,
    fontSize = 58.sp,
    fontWeight = FontWeight.Bold,
)

private val SeparatorStyle = SpanStyle(
    color = white,
    fontSize = 30.sp,
    fontWeight = FontWeight.Bold,
    baselineShift = Superscript,
    letterSpacing = 10.sp
)
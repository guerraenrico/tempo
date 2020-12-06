package com.guerrae.timer.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.SpanStyleRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.BaselineShift.Companion.Superscript
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.resources.FontFamilyMono
import com.enricog.ui_components.resources.white

@Composable
internal fun Clock(backgroundColor: Color, timeInSeconds: Int) {
    val shape = CircleShape
    Column(
        modifier = Modifier
            .shadow(elevation = 20.dp, shape = shape)
            .background(color = backgroundColor, shape = shape)
            .height(220.dp)
            .width(220.dp),
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
    var format = "%01d"
    val spanStyles = mutableListOf<SpanStyleRange>()
    var from = 0
    var to = 0

    if (minutes > 0) {
        format = "%02d"
        val minutesString = String.format(format, minutes)
        to = minutesString.length
        spanStyles.add(SpanStyleRange(NumberStyle, 0, to))
        timeBuilder.append(minutesString)

        from = to
        to += 1
        spanStyles.add(SpanStyleRange(SeparatorStyle, from, to))
        timeBuilder.append(":")
        from += 1
    }
    val secondsString = String.format(format, seconds)
    to += secondsString.length
    timeBuilder.append(secondsString)
    spanStyles.add(SpanStyleRange(NumberStyle, from, to))

    Text(
        text = AnnotatedString(
            text = timeBuilder.toString(),
            spanStyles = spanStyles,
        ),
        textAlign = TextAlign.Center,
    )
}

private val NumberStyle = SpanStyle(
    color = white,
    fontSize = 65.sp,
    fontFamily = FontFamilyMono,
    fontWeight = FontWeight.ExtraBold,
)

private val SeparatorStyle = SpanStyle(
    color = white,
    fontSize = 30.sp,
    fontFamily = FontFamilyMono,
    fontWeight = FontWeight.ExtraBold,
    baselineShift =  BaselineShift(0.8f),
    letterSpacing = 2.sp
)
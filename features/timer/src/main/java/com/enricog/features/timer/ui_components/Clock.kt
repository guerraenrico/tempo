package com.enricog.features.timer.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.FontFamilyMono
import com.enricog.ui.theme.white

internal const val ClockTestTag = "ClockTestTag"
internal const val ClockTimeTextTestTag = "ClockTimeTextTestTag"

@Composable
internal fun Clock(
    modifier: Modifier = Modifier,
    timeInSeconds: Long,
) {
    Column(
        modifier = modifier.testTag(ClockTestTag),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimeText(timeInSeconds = timeInSeconds)
    }
}

@Composable
private fun TimeText(timeInSeconds: Long) {
    val timeText = remember(timeInSeconds) { buildTimeText(timeInSeconds) }
    TempoText(
        text = timeText,
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag(ClockTimeTextTestTag)
    )
}

private val NumberStyle = SpanStyle(
    color = white,
    fontSize = 110.sp,
    fontFamily = FontFamilyMono,
    fontWeight = FontWeight.ExtraBold,
)

private val SeparatorStyle = SpanStyle(
    color = white,
    fontSize = 55.sp,
    fontFamily = FontFamilyMono,
    fontWeight = FontWeight.ExtraBold,
    baselineShift = BaselineShift(0.8f),
    letterSpacing = 2.sp
)

private fun buildTimeText(timeInSeconds: Long): AnnotatedString {

    val minutes = timeInSeconds / 60
    val seconds = timeInSeconds - (minutes * 60)

    val timeBuilder = StringBuilder()
    var format = "%01d"
    val spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    var from = 0
    var to = 0

    if (minutes > 0) {
        format = "%02d"
        val minutesString = String.format(format, minutes)
        to = minutesString.length
        spanStyles.add(AnnotatedString.Range(NumberStyle, 0, to))
        timeBuilder.append(minutesString)

        from = to
        to += 1
        spanStyles.add(AnnotatedString.Range(SeparatorStyle, from, to))
        timeBuilder.append(":")
        from += 1
    }
    val secondsString = String.format(format, seconds)
    to += secondsString.length
    timeBuilder.append(secondsString)
    spanStyles.add(AnnotatedString.Range(NumberStyle, from, to))

    return AnnotatedString(text = timeBuilder.toString(), spanStyles = spanStyles)
}

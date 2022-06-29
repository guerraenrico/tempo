package com.enricog.features.timer.ui_components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import com.enricog.entities.Seconds
import com.enricog.ui.components.shadow.Shadow
import com.enricog.ui.theme.TempoTypography
import com.enricog.ui.theme.white

internal const val ClockTestTag = "ClockTestTag"
internal const val ClockTimeTextTestTag = "ClockTimeTextTestTag"

@Composable
internal fun Clock(modifier: Modifier = Modifier, backgroundColor: Color, seconds: Seconds, size: Dp) {
    val animatedBackgroundColor by animateColorAsState(backgroundColor)

    Box(modifier = modifier) {
        Shadow(size = size / 100 * 10) {
            Column(
                modifier = Modifier
                    .testTag(ClockTestTag)
                    .background(color = animatedBackgroundColor, shape = CircleShape)
                    .height(size)
                    .width(size),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TimeText(timeInSeconds = seconds.value)
            }
        }
    }
}

@Composable
private fun TimeText(timeInSeconds: Long) {
    val timeText = remember(timeInSeconds) { buildTimeText(timeInSeconds) }
    Text(
        text = timeText,
        textAlign = TextAlign.Center,
        modifier = Modifier.testTag(ClockTimeTextTestTag)
    )
}

private val NumberStyle = SpanStyle(
    color = white,
    fontSize = 65.sp,
    fontFamily = TempoTypography.defaultFontFamilyMono,
    fontWeight = FontWeight.ExtraBold,
)

private val SeparatorStyle = SpanStyle(
    color = white,
    fontSize = 30.sp,
    fontFamily = TempoTypography.defaultFontFamilyMono,
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

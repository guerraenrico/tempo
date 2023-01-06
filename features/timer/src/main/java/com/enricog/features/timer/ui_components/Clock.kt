package com.enricog.features.timer.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.enricog.core.compose.api.ScreenConfiguration
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.LANDSCAPE
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.PORTRAIT
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.FontFamilyMono

internal const val ClockTestTag = "ClockTestTag"
internal const val ClockTimeTextTestTag = "ClockTimeTextTestTag"

@Composable
internal fun Clock(
    timeInSeconds: Long,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.testTag(ClockTestTag),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TimeText(timeInSeconds = timeInSeconds, textColor = textColor)
    }
}

@Composable
private fun TimeText(timeInSeconds: Long, textColor: Color) {
    val orientation = ScreenConfiguration.orientation
    val numberStyle = when (orientation) {
        PORTRAIT -> BaseNumberStyle.copy(fontSize = 110.sp)
        LANDSCAPE -> BaseNumberStyle.copy(fontSize = 170.sp)
    }
    val separatorStyle = when (orientation) {
        PORTRAIT -> BaseSeparatorStyle.copy(fontSize = 55.sp)
        LANDSCAPE -> BaseSeparatorStyle.copy(fontSize = 85.sp)
    }
    val timeText = remember(timeInSeconds) {
        buildTimeText(
            timeInSeconds = timeInSeconds,
            numberStyle = numberStyle,
            separatorStyle = separatorStyle
        )
    }
    TempoText(
        text = timeText,
        textAlign = TextAlign.Center,
        style = TextStyle(color = textColor),
        modifier = Modifier.testTag(ClockTimeTextTestTag)
    )
}

private val BaseNumberStyle = SpanStyle(
    fontFamily = FontFamilyMono,
    fontWeight = FontWeight.ExtraBold,
)

private val BaseSeparatorStyle = SpanStyle(
    fontFamily = FontFamilyMono,
    fontWeight = FontWeight.ExtraBold,
    baselineShift = BaselineShift(0.8f),
    letterSpacing = 2.sp
)

private fun buildTimeText(
    timeInSeconds: Long,
    numberStyle: SpanStyle,
    separatorStyle: SpanStyle
): AnnotatedString {

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
        spanStyles.add(AnnotatedString.Range(numberStyle, 0, to))
        timeBuilder.append(minutesString)

        from = to
        to += 1
        spanStyles.add(AnnotatedString.Range(separatorStyle, from, to))
        timeBuilder.append(":")
        from += 1
    }
    val secondsString = String.format(format, seconds)
    to += secondsString.length
    timeBuilder.append(secondsString)
    spanStyles.add(AnnotatedString.Range(numberStyle, from, to))

    return AnnotatedString(text = timeBuilder.toString(), spanStyles = spanStyles)
}

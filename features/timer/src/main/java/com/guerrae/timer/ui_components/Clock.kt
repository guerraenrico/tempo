package com.guerrae.timer.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.resources.Prompt
import com.enricog.ui_components.resources.TempoTheme.colors
import com.enricog.ui_components.resources.TempoTheme.typography
import com.enricog.ui_components.resources.white

@Composable
internal fun Clock(backgroundColor: Color, timeInSeconds: Int) {
    val shape = CircleShape
    Column(
        modifier = Modifier
            .drawShadow(elevation = 20.dp, shape = shape)
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
    Row(
        modifier = Modifier.background(colors.background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (minutes >= 0) {
            Number(minutes)
            Separator()
        }
        Number2(seconds)
    }
}

@Composable
private fun Number(value: Int) {
    BasicText(
        text = value.toString(),
        style = typography.title.copy(
            fontWeight = FontWeight.Bold,
            color = white,
            fontSize = 55.sp,
            lineHeight = 0.sp
        )
    )
}


@Composable
private fun Number2(value: Int) {
    BasicText(
        text = value.toString(),
        style =  TextStyle(
            fontFamily = Prompt,
            fontWeight = FontWeight.Bold,
            color = white,
            fontSize = 55.sp
        )
    )
}


@Composable
private fun Separator() {
    BasicText(
        text = ":",
        style = typography.title.copy(
            fontWeight = FontWeight.Bold,
            color = white,
            fontSize = 30.sp,
            lineHeight = 0.sp
        )
    )
}

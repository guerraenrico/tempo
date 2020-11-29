package com.enricog.ui_components.resources

import androidx.compose.runtime.Stable
import androidx.compose.runtime.staticAmbientOf
import androidx.compose.ui.graphics.Color

val darkBlue500 = Color(0xff1A1D20)

val blue500 = Color(0xff006DFF)

val green500 = Color(0xff32B84D)

val orange500 = Color(0xffC7600D)

val purple500 = Color(0xff722CCE)

val white = Color(0xffffffff)

@Stable
data class TempoColors(
    val primaryText: Color,
    val accent: Color,
    val background: Color
)

internal val defaultThemeColors = TempoColors(
    primaryText = white,
    accent = blue500,
    background = darkBlue500
)

internal val AmbientColors = staticAmbientOf { defaultThemeColors }
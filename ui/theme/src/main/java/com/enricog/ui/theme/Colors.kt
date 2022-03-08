package com.enricog.ui.theme

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val darkBlue200 = Color(0xff_5D6171)
val darkBlue400 = Color(0xff_212529)
val darkBlue500 = Color(0xff_1A1D20)
val darkBlue600 = Color(0xff_171A1D)

val blue500 = Color(0xff_246BFD)
val blue600 = Color(0xff_125DF8)

val green500 = Color(0xff_4CAF50)
val green600 = Color(0xff_43A047)

val grey100 = Color(0xff_FAFAFA)

val white = Color(0xff_FFFFFF)
val black = Color(0xff_000000)

val red = Color(0xff_F44336)

internal val defaultThemeColors = Colors(
    primary = blue500,
    primaryVariant = blue500,
    secondary = green500,
    secondaryVariant = green500,
    background = darkBlue500,
    surface = darkBlue400,
    error = red,
    onPrimary = white,
    onSecondary = white,
    onBackground = white,
    onSurface = white,
    onError = white,
    isLight = false
)

object TimeTypeColors {
    val STARTING = Color(0xff_8E96FF)
    val STOPWATCH = Color(0xff_A06AFA)
    val TIMER = Color(0xff_4DB4E9)
    val REST = Color(0xff_FF968E)
}

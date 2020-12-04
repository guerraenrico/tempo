package com.enricog.ui_components.resources

import androidx.compose.material.Colors
import androidx.compose.ui.graphics.Color

val darkBlue500 = Color(0xff_1A1D20)

val blue500 = Color(0xff_006DFF)

val green500 = Color(0xff_32B84D)

val orange500 = Color(0xff_C7600D)

val purple500 = Color(0xff_722CCE)

val white = Color(0xff_ffffff)

val red = Color(0xff_cf6679)

internal val defaultThemeColors = Colors(
    primary = blue500,
    primaryVariant = blue500,
    secondary = green500,
    secondaryVariant = green500,
    background = darkBlue500,
    surface = darkBlue500,
    error = red,
    onPrimary = white,
    onSecondary = white,
    onBackground = white,
    onSurface = white,
    onError = white,
    isLight = false
)
package com.enricog.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
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
val grey200 = Color(0xff_B4B7C2)

val white = Color(0xff_FFFFFF)
val black = Color(0xff_000000)

val red = Color(0xff_F56C62)

@Immutable
class TempoColors internal constructor() {

    val primary: Color = blue500
    val primaryVariant: Color = blue500
    val secondary: Color = green500
    val secondaryVariant: Color = green500
    val background: Color = darkBlue500
    val surface: Color = darkBlue400
    val error: Color = red
    val onPrimary: Color = white
    val onSecondary: Color = white
    val onBackground: Color = white
    val onSurface: Color = white
    val onSurfaceSecondary: Color = grey200
    val onError: Color = darkBlue500
    val isLight: Boolean = false

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TempoColors

        if (primary != other.primary) return false
        if (primaryVariant != other.primaryVariant) return false
        if (secondary != other.secondary) return false
        if (secondaryVariant != other.secondaryVariant) return false
        if (background != other.background) return false
        if (surface != other.surface) return false
        if (error != other.error) return false
        if (onPrimary != other.onPrimary) return false
        if (onSecondary != other.onSecondary) return false
        if (onBackground != other.onBackground) return false
        if (onSurface != other.onSurface) return false
        if (onSurfaceSecondary != other.onSurfaceSecondary) return false
        if (onError != other.onError) return false
        if (isLight != other.isLight) return false

        return true
    }

    override fun hashCode(): Int {
        var result = primary.hashCode()
        result = 31 * result + primaryVariant.hashCode()
        result = 31 * result + secondary.hashCode()
        result = 31 * result + secondaryVariant.hashCode()
        result = 31 * result + background.hashCode()
        result = 31 * result + surface.hashCode()
        result = 31 * result + error.hashCode()
        result = 31 * result + onPrimary.hashCode()
        result = 31 * result + onSecondary.hashCode()
        result = 31 * result + onBackground.hashCode()
        result = 31 * result + onSurface.hashCode()
        result = 31 * result + onSurfaceSecondary.hashCode()
        result = 31 * result + onError.hashCode()
        result = 31 * result + isLight.hashCode()
        return result
    }

    override fun toString(): String {
        return "TempoColors(primary=$primary, primaryVariant=$primaryVariant, secondary=$secondary," +
                " secondaryVariant=$secondaryVariant, background=$background, surface=$surface, " +
                "error=$error, onPrimary=$onPrimary, onSecondary=$onSecondary, onBackground=$onBackground, " +
                "onSurface=$onSurface, onSurfaceSecondary=$onSurfaceSecondary, onError=$onError, isLight=$isLight)"
    }
}

fun TempoColors.contentColorFor(backgroundColor: Color): Color {
    return when (backgroundColor) {
        primary -> onPrimary
        primaryVariant -> onPrimary
        secondary -> onSecondary
        secondaryVariant -> onSecondary
        background -> onBackground
        surface -> onSurface
        error -> onError
        TimeTypeColors.STARTING -> darkBlue500
        TimeTypeColors.STOPWATCH -> darkBlue500
        TimeTypeColors.TIMER -> darkBlue500
        TimeTypeColors.REST -> darkBlue500
        else -> Color.Unspecified
    }
}

internal val LocalTempoColors = staticCompositionLocalOf { TempoColors() }

internal fun TempoColors.toMaterialColors(): Colors {
    return Colors(
        primary = primary,
        primaryVariant = primaryVariant,
        secondary = secondary,
        secondaryVariant = secondaryVariant,
        background = background,
        surface = surface,
        error = error,
        onPrimary = onPrimary,
        onSecondary = onSecondary,
        onBackground = onBackground,
        onSurface = onSurface,
        onError = onError,
        isLight = isLight
    )
}

object TimeTypeColors {
    val STARTING = Color(0xff_A0A6FF)
    val STOPWATCH = Color(0xff_BC92FF)
    val TIMER = Color(0xff_57C0F5)
    val REST = Color(0xff_FF968E)
}

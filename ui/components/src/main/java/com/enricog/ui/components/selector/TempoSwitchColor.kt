package com.enricog.ui.components.selector

import androidx.compose.material.ContentAlpha
import androidx.compose.material.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import com.enricog.ui.theme.TempoTheme

object TempoSwitchColor {

    @Composable
    internal fun colors(): TempoSwitchColors {
        return TempoSwitchColors(
            checkedThumbColor = TempoTheme.colors.primaryVariant,
            checkedTrackColor = TempoTheme.colors.primaryVariant.copy(alpha = 0.54f),
            uncheckedThumbColor = TempoTheme.colors.surface,
            uncheckedTrackColor = TempoTheme.colors.onSurface.copy(alpha = 0.54f),
            disabledCheckedThumbColor = TempoTheme.colors.primaryVariant
                .copy(alpha = ContentAlpha.disabled)
                .compositeOver(TempoTheme.colors.surface),
            disabledCheckedTrackColor = TempoTheme.colors.primaryVariant
                .copy(alpha = ContentAlpha.disabled)
                .compositeOver(TempoTheme.colors.surface),
            disabledUncheckedThumbColor = TempoTheme.colors.surface
                .copy(alpha = ContentAlpha.disabled)
                .compositeOver(TempoTheme.colors.surface),
            disabledUncheckedTrackColor = TempoTheme.colors.surface
                .copy(alpha = ContentAlpha.disabled)
                .compositeOver(TempoTheme.colors.surface),
        )
    }
}

@Immutable
internal data class TempoSwitchColors(
    private val checkedThumbColor: Color,
    private val checkedTrackColor: Color,
    private val uncheckedThumbColor: Color,
    private val uncheckedTrackColor: Color,
    private val disabledCheckedThumbColor: Color,
    private val disabledCheckedTrackColor: Color,
    private val disabledUncheckedThumbColor: Color,
    private val disabledUncheckedTrackColor: Color
) : SwitchColors {

    @Composable
    override fun thumbColor(enabled: Boolean, checked: Boolean): State<Color> {
        val color = if (enabled) {
            if (checked) checkedThumbColor else uncheckedThumbColor
        } else {
            if (checked) disabledCheckedThumbColor else disabledUncheckedThumbColor
        }
        return rememberUpdatedState(newValue = color)
    }

    @Composable
    override fun trackColor(enabled: Boolean, checked: Boolean): State<Color> {
        val color = if (enabled) {
            if (checked) checkedTrackColor else uncheckedTrackColor
        } else {
            if (checked) disabledCheckedTrackColor else disabledUncheckedTrackColor
        }
        return rememberUpdatedState(newValue = color)
    }
}
package com.enricog.ui.components.button

import androidx.compose.material.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.enricog.ui.theme.blue500
import com.enricog.ui.theme.blue600
import com.enricog.ui.theme.darkBlue200
import com.enricog.ui.theme.darkBlue500
import com.enricog.ui.theme.darkBlue600
import com.enricog.ui.theme.green500
import com.enricog.ui.theme.green600
import com.enricog.ui.theme.grey100
import com.enricog.ui.theme.white

sealed class TempoButtonColor {

    @Composable
    internal abstract fun buttonColors(): TempoButtonColors

    object TransparentSecondary : TempoButtonColor() {

        @Composable
        override fun buttonColors(): TempoButtonColors = TempoButtonColors(
            enabledBackgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            enabledContentColor = darkBlue200,
            disabledContentColor = grey100
        )
    }

    object TransparentPrimary : TempoButtonColor() {

        @Composable
        override fun buttonColors(): TempoButtonColors = TempoButtonColors(
            enabledBackgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent,
            enabledContentColor = white,
            disabledContentColor = grey100
        )
    }

    object Normal : TempoButtonColor() {

        @Composable
        override fun buttonColors(): TempoButtonColors = TempoButtonColors(
            enabledBackgroundColor = darkBlue500,
            disabledBackgroundColor = darkBlue600,
            enabledContentColor = white,
            disabledContentColor = grey100
        )
    }

    object Confirm : TempoButtonColor() {

        @Composable
        override fun buttonColors(): TempoButtonColors = TempoButtonColors(
            enabledBackgroundColor = green500,
            disabledBackgroundColor = green600,
            enabledContentColor = white,
            disabledContentColor = grey100
        )
    }

    object Accent : TempoButtonColor() {

        @Composable
        override fun buttonColors(): TempoButtonColors = TempoButtonColors(
            enabledBackgroundColor = blue500,
            disabledBackgroundColor = blue600,
            enabledContentColor = white,
            disabledContentColor = grey100
        )
    }
}

internal data class TempoButtonColors(
    private val enabledBackgroundColor: Color,
    private val disabledBackgroundColor: Color,
    private val enabledContentColor: Color,
    private val disabledContentColor: Color
) : ButtonColors {

    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) enabledBackgroundColor else disabledBackgroundColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) enabledContentColor else disabledContentColor)
    }
}

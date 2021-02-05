package com.enricog.ui_components.common.button

import androidx.compose.material.ButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.graphics.Color
import com.enricog.ui_components.resources.*

abstract class TempoButtonColors : ButtonColors {
    abstract val enabledBackgroundColor: Color
    abstract val disabledBackgroundColor: Color

    abstract val enabledContentColor: Color
    abstract val disabledContentColor: Color

    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) enabledBackgroundColor else disabledBackgroundColor)
    }

    @Composable
    override fun contentColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) enabledContentColor else disabledContentColor)
    }
}

sealed class TempoButtonColor : TempoButtonColors() {
    object TransparentSecondary : TempoButtonColor() {
        override val enabledBackgroundColor: Color = Color.Transparent
        override val disabledBackgroundColor: Color = Color.Transparent
        override val enabledContentColor: Color = darkBlue200
        override val disabledContentColor: Color = grey100
    }

    object TransparentPrimary : TempoButtonColor() {
        override val enabledBackgroundColor: Color = Color.Transparent
        override val disabledBackgroundColor: Color = Color.Transparent
        override val enabledContentColor: Color = white
        override val disabledContentColor: Color = grey100
    }

    object Normal : TempoButtonColor() {
        override val enabledBackgroundColor: Color = darkBlue500
        override val disabledBackgroundColor: Color = darkBlue600
        override val enabledContentColor: Color = white
        override val disabledContentColor: Color = grey100
    }

    object Confirm : TempoButtonColor() {
        override val enabledBackgroundColor: Color = green500
        override val disabledBackgroundColor: Color = green600
        override val enabledContentColor: Color = white
        override val disabledContentColor: Color = grey100
    }

    object Accent : TempoButtonColor() {
        override val enabledBackgroundColor: Color = blue500
        override val disabledBackgroundColor: Color = blue600
        override val enabledContentColor: Color = white
        override val disabledContentColor: Color = grey100
    }
}

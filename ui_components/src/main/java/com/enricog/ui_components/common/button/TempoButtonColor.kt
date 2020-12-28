package com.enricog.ui_components.common.button

import androidx.compose.material.ButtonColors
import androidx.compose.ui.graphics.Color
import com.enricog.ui_components.resources.*

abstract class TempoButtonColors : ButtonColors {
    abstract val enabledBackgroundColor: Color
    abstract val disabledBackgroundColor: Color

    abstract val enabledContentColor: Color
    abstract val disabledContentColor: Color

    override fun backgroundColor(enabled: Boolean): Color {
        return if (enabled) enabledBackgroundColor else disabledBackgroundColor
    }

    override fun contentColor(enabled: Boolean): Color {
        return if (enabled) enabledContentColor else disabledContentColor
    }
}

sealed class TempoButtonColor : TempoButtonColors() {
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

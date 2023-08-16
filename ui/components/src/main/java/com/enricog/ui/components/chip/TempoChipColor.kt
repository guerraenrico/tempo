package com.enricog.ui.components.chip

import androidx.compose.ui.graphics.Color
import com.enricog.ui.theme.darkBlue500
import com.enricog.ui.theme.white

sealed class TempoChipColor {
    abstract val onBackgroundColor: Color
    abstract val backgroundColor: Color

    object Default : TempoChipColor() {
        override val onBackgroundColor: Color = darkBlue500
        override val backgroundColor: Color = white

    }

    data class Adaptive(
        override val onBackgroundColor: Color,
        override val backgroundColor: Color
    ): TempoChipColor()
}
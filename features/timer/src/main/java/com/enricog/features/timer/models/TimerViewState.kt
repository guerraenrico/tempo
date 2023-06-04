package com.enricog.features.timer.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.textField.TimeText

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val timeInSeconds: Long,
        @StringRes val stepTitleId: Int,
        val segmentName: String,
        val clockBackground: Background,
        val clockOnBackgroundColor: Color,
        val timerActions: Actions
    ) : TimerViewState() {

        data class Background(
            val background: Color,
            val ripple: Color?
        )

        data class Actions(
            val next: Button,
            val back: Button,
            val play: Button
        ) {
            data class Button(
                @DrawableRes val iconResId: Int,
                @StringRes val contentDescriptionResId: Int,
                val size: TempoIconButtonSize
            )
        }
    }

    data class Completed(
        val effectiveTotalTime: TimeText,
        val skipCount: Int
    ) : TimerViewState()

    data class Error(val throwable: Throwable) : TimerViewState()
}

package com.enricog.features.timer.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.enricog.ui.components.button.icon.TempoIconButtonSize

internal sealed class TimerViewState {

    object Idle : TimerViewState()

    data class Counting(
        val timeInSeconds: Long,
        @StringRes val stepTitleId: Int,
        val segmentName: String,
        val clockBackgroundColor: BackgroundColor,
        val isSoundEnabled: Boolean,
        val timerActions: Actions
    ) : TimerViewState() {

        data class BackgroundColor(
            val foreground: Color,
            val ripple: Color?
        )

        data class Actions(
            val restart: Button,
            val toggleStart: Button
        ) {
            data class Button(
                @DrawableRes val iconResId: Int,
                @StringRes val contentDescriptionResId: Int,
                val size: TempoIconButtonSize
            )
        }
    }

    object Completed : TimerViewState()

    data class Error(val throwable: Throwable) : TimerViewState()
}

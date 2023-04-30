package com.enricog.features.timer.service

import android.graphics.Color
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal sealed class TimerServiceViewState {

    object Idle : TimerServiceViewState()

    data class Counting(
        val time: String,
        @StringRes val stepTitleId: Int,
        val segmentName: String,
        val clockBackground: Background,
        val clockOnBackgroundColor: Color,
        val timerActions: Actions
    ) : TimerServiceViewState() {

        data class Background(val background: Color)

        data class Actions(val play: Button) {
            data class Button(
                @DrawableRes val iconResId: Int,
                @StringRes val contentDescriptionResId: Int
            )
        }
    }

    object Completed : TimerServiceViewState()

    data class Error(val throwable: Throwable) : TimerServiceViewState()

}
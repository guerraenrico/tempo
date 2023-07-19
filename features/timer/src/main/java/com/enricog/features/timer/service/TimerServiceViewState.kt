package com.enricog.features.timer.service

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

internal sealed class TimerServiceViewState {

    object Idle : TimerServiceViewState()

    data class Counting(
        val time: String,
        @StringRes val stepTitleId: Int,
        val segmentName: String,
        val routineRoundText: RoundText?,
        val segmentRoundText: RoundText?,
        val clockBackground: Background,
        val clockOnBackgroundColor: Long,
        val timerActions: Actions
    ) : TimerServiceViewState() {

        data class Background(val background: Long)

        data class Actions(val play: Button) {
            data class Button(
                @DrawableRes val iconResId: Int,
                @StringRes val contentDescriptionResId: Int
            )
        }

        data class RoundText(@StringRes val labelId: Int, val formatArgs: List<Any>)
    }

    object Completed : TimerServiceViewState()

    data class Error(val throwable: Throwable) : TimerServiceViewState()

}
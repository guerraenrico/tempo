package com.guerrae.timer

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.guerrae.base_android.viewmodel.BaseViewModel
import com.guerrae.timer.models.TimerState
import com.guerrae.timer.models.TimerViewState

internal class TimerViewModel @ViewModelInject constructor(
    converter: TimerStateConverter
) : BaseViewModel<TimerState, TimerViewState>(
    initialState = TimerState.Idle,
    converter = converter
) {
}
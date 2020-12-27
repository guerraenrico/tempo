package com.enricog.timer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerConfiguration
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.CountingScene
import com.enricog.ui_components.extensions.viewModel

@Composable
internal fun Timer(routineId: Long, viewModel: TimerViewModel = viewModel()) {
    onActive {
        viewModel.load(TimerConfiguration(routineId))
    }
    val viewState by viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        viewState.Compose(viewModel)
    }

    onDispose { toggleKeepScreenOnFlag(enable = false) }
}

@Composable
internal fun TimerViewState.Compose(timerActions: TimerActions) {
    when (this) {
        TimerViewState.Idle -> {
        }
        is TimerViewState.Counting -> {
            toggleKeepScreenOnFlag(enableKeepScreenOn)

            CountingScene(state = this, timerActions = timerActions)
        }
    }.exhaustive
}

private fun toggleKeepScreenOnFlag(enable: Boolean) {
//    if (enable) {
//        requireActivity().window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//    } else {
//        requireActivity().window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
//    }
}
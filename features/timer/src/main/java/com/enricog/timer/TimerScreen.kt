package com.enricog.timer

import android.app.Activity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.enricog.core.extensions.exhaustive
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerConfiguration
import com.enricog.timer.models.TimerViewState
import com.enricog.timer.ui_components.CountingScene
import com.enricog.ui_components.ambients.TempoAmbient
import com.enricog.ui_components.ambients.navViewModel

@Composable
internal fun TimerScreen(routineId: Long, viewModel: TimerViewModel = navViewModel()) {
    onActive {
        viewModel.load(TimerConfiguration(routineId))
    }
    val viewState by viewModel.viewState.collectAsState(TimerViewState.Idle)
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        viewState.Compose(viewModel)
    }

    // fixme other solutions?
//    onDispose { TempoAmbient.activity.toggleKeepScreenOnFlag(enable = false) }
}

@Composable
internal fun TimerViewState.Compose(timerActions: TimerActions) {
    when (this) {
        TimerViewState.Idle -> {
        }
        is TimerViewState.Counting -> {
            TempoAmbient.activity.toggleKeepScreenOnFlag(enableKeepScreenOn)

            CountingScene(state = this, timerActions = timerActions)
        }
    }.exhaustive
}

private fun Activity.toggleKeepScreenOnFlag(enable: Boolean) {
    if (enable) {
        window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
        window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
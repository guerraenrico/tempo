package com.enricog.features.timer.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.enricog.features.timer.settings.models.TimerSettingsViewState
import com.enricog.features.timer.settings.ui_components.TimerSettingsScene
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun TimerSettingsScreen(viewModel: TimerSettingsViewModel) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle(TimerSettingsViewState.Idle)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(TempoTheme.dimensions.spaceM)
    ) {
        viewState.Compose(
            onSoundClick = viewModel::onToggleSound,
            onRunInBackgroundClick = viewModel::onToggleRunInBackground
        )
    }
}

@Composable
internal fun TimerSettingsViewState.Compose(
    onSoundClick: () -> Unit,
    onRunInBackgroundClick: () -> Unit
) {
    when (this) {
        TimerSettingsViewState.Idle -> Unit
        is TimerSettingsViewState.Data -> TimerSettingsScene(
            state = this,
            onSoundClick = onSoundClick,
            onRunInBackgroundClick = onRunInBackgroundClick
        )
    }
}
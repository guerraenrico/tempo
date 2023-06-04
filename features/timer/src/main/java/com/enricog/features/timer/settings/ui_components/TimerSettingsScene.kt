package com.enricog.features.timer.settings.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.features.timer.settings.models.TimerSettingsViewState
import com.enricog.ui.components.selector.TempoSwitch
import com.enricog.ui.components.text.TempoText

internal const val TimerSettingSceneTestTag = "TimerSettingSceneTestTag"
internal const val TimerSoundSettingButtonTestTag = "TimerSoundSettingButtonTestTag"
internal const val TimerBackgroundSettingButtonTestTag = "TimerBackgroundSettingButtonTestTag"

@Composable
internal fun TimerSettingsScene(
    state: TimerSettingsViewState.Data,
    onSoundClick: () -> Unit,
    onRunInBackgroundClick: () -> Unit
) {
    Column(
        modifier = Modifier.testTag(TimerSettingSceneTestTag)
    ) {
        SoundSetting(soundSetting = state.soundSetting, onClick = onSoundClick)
        BackgroundSetting(runInBackgroundSetting = state.runInBackgroundSetting, onClick = onRunInBackgroundClick)
    }
}

@Composable
private fun SoundSetting(
    soundSetting: TimerSettingsViewState.Data.Setting,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TempoText(
            modifier = Modifier.weight(weight = 1f),
            text = stringResource(id = soundSetting.title)
        )
        TempoSwitch(
            modifier = Modifier.testTag(TimerSoundSettingButtonTestTag),
            checked = soundSetting.enabled,
            onCheckedChange = { onClick() }
        )
    }
}

@Composable
private fun BackgroundSetting(
    runInBackgroundSetting: TimerSettingsViewState.Data.Setting,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TempoText(
            modifier = Modifier.weight(weight = 1f),
            text = stringResource(id = runInBackgroundSetting.title)
        )
        TempoSwitch(
            modifier = Modifier
                .testTag(TimerBackgroundSettingButtonTestTag),
            checked = runInBackgroundSetting.enabled,
            onCheckedChange = { onClick() }
        )
    }
}

package com.enricog.features.timer.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.core.compose.api.ScreenConfiguration
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.LANDSCAPE
import com.enricog.core.compose.api.ScreenConfiguration.Orientation.PORTRAIT
import com.enricog.features.timer.R
import com.enricog.features.timer.models.TimerViewState
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.components.textField.TimeText
import com.enricog.ui.components.textField.timeText
import com.enricog.ui.theme.TempoTheme

internal const val TimerCompletedSceneTestTag = "TimerCompletedSceneTestTag"
internal const val RoutineCompletedTitleTestTag = "RoutineCompletedTitleTestTag"
internal const val RoutineEffectiveTimeTestTag = "RoutineEffectiveTimeTestTag"
internal const val RoutineSkipCountTestTag = "RoutineSkipCountTestTag"
internal const val ButtonDoneTestTag = "ButtonDoneTestTag"
internal const val ButtonResetTestTag = "ButtonResetTestTag"

@Composable
internal fun TimerCompletedScene(
    state: TimerViewState.Completed,
    onReset: () -> Unit,
    onDone: () -> Unit,
) {
    when (ScreenConfiguration.orientation) {
        PORTRAIT -> TimerCompletedScenePortrait(
            state = state,
            onReset = onReset,
            onDone = onDone
        )
        LANDSCAPE -> TimerCompletedSceneLandscape(
            state = state,
            onReset = onReset,
            onDone = onDone
        )
    }
}

@Composable
internal fun TimerCompletedScenePortrait(
    state: TimerViewState.Completed,
    onReset: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TimerCompletedSceneTestTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TempoText(
            modifier = Modifier.testTag(RoutineCompletedTitleTestTag),
            text = stringResource(R.string.title_routine_completed),
            style = TempoTheme.typography.h1.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TotalTimeContainer(effectiveTotalTime = state.effectiveTotalTime)
            SkipCountContainer(skipCount = state.skipCount)
        }
        Spacer(modifier = Modifier.height(50.dp))
        TempoIconButton(
            onClick = onReset,
            iconResId = R.drawable.ic_timer_restart,
            modifier = Modifier.testTag(ButtonResetTestTag),
            contentDescription = stringResource(R.string.content_description_button_reset_routine)
        )
        Spacer(modifier = Modifier.height(30.dp))
        TempoIconButton(
            onClick = onDone,
            iconResId = R.drawable.ic_timer_done,
            color = TempoButtonColor.Accent,
            size = TempoIconButtonSize.Large,
            modifier = Modifier.testTag(ButtonDoneTestTag),
            contentDescription = stringResource(R.string.content_description_button_done_routine)
        )
    }
}

@Composable
internal fun TimerCompletedSceneLandscape(
    state: TimerViewState.Completed,
    onReset: () -> Unit,
    onDone: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TimerCompletedSceneTestTag),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TempoText(
            modifier = Modifier.testTag(RoutineCompletedTitleTestTag),
            text = stringResource(R.string.title_routine_completed),
            style = TempoTheme.typography.h1.copy(
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp
            )
        )
        Spacer(modifier = Modifier.height(50.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TotalTimeContainer(effectiveTotalTime = state.effectiveTotalTime)
            SkipCountContainer(skipCount = state.skipCount)
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                TempoIconButton(
                    onClick = onReset,
                    iconResId = R.drawable.ic_timer_restart,
                    modifier = Modifier.testTag(ButtonResetTestTag),
                    contentDescription = stringResource(R.string.content_description_button_reset_routine)
                )
                Spacer(modifier = Modifier.height(30.dp))
                TempoIconButton(
                    onClick = onDone,
                    iconResId = R.drawable.ic_timer_done,
                    color = TempoButtonColor.Accent,
                    size = TempoIconButtonSize.Large,
                    modifier = Modifier.testTag(ButtonDoneTestTag),
                    contentDescription = stringResource(R.string.content_description_button_done_routine)
                )
            }
        }
    }
}

@Composable
private fun TotalTimeContainer(
    effectiveTotalTime: TimeText,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(width = 130.dp)
            .border(width = 2.dp, color = TempoTheme.colors.surface, shape = TempoTheme.shapes.medium)
            .clip(shape = TempoTheme.shapes.medium)
            .background(color = TempoTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TempoText(
            modifier = Modifier
                .padding(all = TempoTheme.dimensions.spaceS),
            text = stringResource(id = R.string.label_routine_total_time),
            style = TempoTheme.typography.h2,
            textAlign = TextAlign.Center
        )
        TempoText(
            modifier = Modifier
                .testTag(RoutineEffectiveTimeTestTag)
                .padding(all = TempoTheme.dimensions.spaceS),
            text = effectiveTotalTime.toPrettyString(),
            style = TempoTheme.typography.h1,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun SkipCountContainer(skipCount: Int, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(width = 130.dp)
            .border(width = 2.dp, color = TempoTheme.colors.surface, shape = TempoTheme.shapes.medium)
            .clip(shape = TempoTheme.shapes.medium)
            .background(color = TempoTheme.colors.surface),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TempoText(
            modifier = Modifier
                .padding(all = TempoTheme.dimensions.spaceS),
            text = stringResource(id = R.string.label_routine_skip_count),
            style = TempoTheme.typography.h2,
            textAlign = TextAlign.Center
        )
        TempoText(
            modifier = Modifier
                .testTag(RoutineSkipCountTestTag)
                .padding(all = TempoTheme.dimensions.spaceS),
            text = skipCount.toString(),
            style = TempoTheme.typography.h1,
            textAlign = TextAlign.Center
        )
    }
}

@Preview
@Composable
private fun Preview() {
    TimerCompletedScene(
        state = TimerViewState.Completed(
            effectiveTotalTime = "12:35".timeText,
            skipCount = 10
        ),
        onReset = {},
        onDone = {}
    )
}
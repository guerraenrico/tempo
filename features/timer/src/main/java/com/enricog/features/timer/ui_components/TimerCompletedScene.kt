package com.enricog.features.timer.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.features.timer.R
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val TimerCompletedSceneTestTag = "TimerCompletedSceneTestTag"
internal const val RoutineCompletedTitleTestTag = "RoutineCompletedTitleTestTag"
internal const val ButtonDoneTestTag = "ButtonDoneTestTag"
internal const val ButtonResetTestTag = "ButtonResetTestTag"

@Composable
internal fun TimerCompletedScene(
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
        Spacer(modifier = Modifier.height(40.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            TempoIconButton(
                onClick = onReset,
                icon = painterResource(R.drawable.ic_timer_restart),
                modifier = Modifier.testTag(ButtonResetTestTag),
                contentDescription = stringResource(R.string.content_description_button_reset_routine)
            )
            Spacer(modifier = Modifier.width(20.dp))
            TempoIconButton(
                onClick = onDone,
                icon = painterResource(R.drawable.ic_timer_done),
                color = TempoButtonColor.Accent,
                modifier = Modifier.testTag(ButtonDoneTestTag),
                contentDescription = stringResource(R.string.content_description_button_done_routine)
            )
        }
    }
}
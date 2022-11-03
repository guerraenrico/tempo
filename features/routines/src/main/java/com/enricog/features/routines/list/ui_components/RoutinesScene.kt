package com.enricog.features.routines.list.ui_components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.R
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Message
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.snackbar.TempoSnackbarEvent
import com.enricog.ui.components.snackbar.TempoSnackbarHost
import com.enricog.ui.components.snackbar.rememberSnackbarHostState
import com.enricog.ui.theme.TempoTheme

internal const val RoutinesSceneTestTag = "RoutinesSceneTestTag"

@Composable
internal fun RoutinesScene(
    routines: List<Routine>,
    message: Message?,
    onRoutine: (Routine) -> Unit,
    onRoutineDelete: (Routine) -> Unit,
    onCreateRoutine: () -> Unit,
    onSnackbarEvent: (TempoSnackbarEvent) -> Unit
) {
    val snackbarHostState = rememberSnackbarHostState()

    if (message != null) {
        val messageText = stringResource(id = message.textResId)
        val actionText = stringResource(id = message.actionTextResId)
        LaunchedEffect(snackbarHostState) {
            val event = snackbarHostState.show(message = messageText, actionText = actionText)
            onSnackbarEvent(event)
        }
    }

    TempoSnackbarHost(
        state = snackbarHostState,
        content = {
            LazyColumn(
                modifier = Modifier
                    .testTag(RoutinesSceneTestTag)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(TempoTheme.dimensions.spaceM),
                contentPadding = PaddingValues(TempoTheme.dimensions.spaceM)
            ) {
                items(
                    items = routines,
                    key = { routine -> routine.id.toLong() }
                ) { routine ->
                    RoutineItem(
                        modifier = Modifier.fillMaxWidth(),
                        routine = routine,
                        onClick = onRoutine,
                        onDelete = onRoutineDelete
                    )
                }
            }
        },
        anchor = {
            Box(modifier = Modifier.fillMaxWidth()) {
                TempoIconButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(TempoTheme.dimensions.spaceM),
                    onClick = onCreateRoutine,
                    icon = painterResource(R.drawable.ic_add),
                    color = TempoButtonColor.Accent,
                    contentDescription = stringResource(R.string.content_description_button_create_routine)
                )
            }
        }
    )
}

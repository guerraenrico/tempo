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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.enricog.data.routines.api.entities.Routine
import com.enricog.features.routines.R
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.theme.TempoTheme

internal const val RoutinesSceneTestTag = "RoutinesSceneTestTag"

@Composable
internal fun RoutinesScene(
    routines: List<Routine>,
    onRoutineClick: (Routine) -> Unit,
    onRoutineDelete: (Routine) -> Unit,
    onCreateRoutineClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .testTag(RoutinesSceneTestTag)
            .fillMaxSize()

    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
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
                    onClick = onRoutineClick,
                    onDelete = onRoutineDelete
                )
            }
        }

        TempoIconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(TempoTheme.dimensions.spaceL),
            onClick = onCreateRoutineClick,
            icon = painterResource(R.drawable.ic_add),
            color = TempoButtonColor.Accent,
            contentDescription = stringResource(R.string.content_description_button_create_routine)
        )
    }
}

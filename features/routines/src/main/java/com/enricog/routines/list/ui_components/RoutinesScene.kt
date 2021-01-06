package com.enricog.routines.list.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import com.enricog.entities.routines.Routine
import com.enricog.routines.R
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.resources.dimensions

internal const val RoutineSceneTestTag = "RoutineSceneTestTag"

@Composable
internal fun RoutinesScene(
    routines: List<Routine>,
    onRoutineClick: (Routine) -> Unit,
    onRoutineDelete: (Routine) -> Unit,
    onCreateRoutineClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .testTag(RoutineSceneTestTag)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimensions.spaceM),
            contentPadding = PaddingValues(MaterialTheme.dimensions.spaceM)
        ) {
            items(routines) { routine: Routine ->
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
                .padding(MaterialTheme.dimensions.spaceL),
            onClick = onCreateRoutineClick,
            icon = vectorResource(R.drawable.ic_segment_add),
            color = TempoButtonColor.Accent
        )
    }
}
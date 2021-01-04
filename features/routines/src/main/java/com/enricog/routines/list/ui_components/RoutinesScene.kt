package com.enricog.routines.list.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import com.enricog.ui_components.modifiers.verticalListItemSpacing
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
                .fillMaxSize()
        ) {
            itemsIndexed(routines) { index: Int, routine: Routine ->
                RoutineItem(
                    routine = routine,
                    onClick = onRoutineClick,
                    onDelete = onRoutineDelete,
                    modifier = Modifier.fillMaxWidth()
                        .verticalListItemSpacing(
                            itemPosition = index,
                            spacing = MaterialTheme.dimensions.spaceM
                        )
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
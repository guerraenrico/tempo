package com.enricog.routines.list.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Routine
import com.enricog.routines.ui_components.DeletableListItem
import com.enricog.ui_components.resources.TempoTheme

internal const val RoutineItemTestTag = "RoutineItemTestTag"

@Composable
internal fun RoutineItem(
    modifier: Modifier = Modifier,
    routine: Routine,
    onClick: (Routine) -> Unit,
    onDelete: (Routine) -> Unit
) {
    DeletableListItem(
        modifier = modifier
            .testTag(RoutineItemTestTag),
        onDelete = { onDelete(routine) }
    ) {
        Box(
            modifier = Modifier
                .clickable { onClick(routine) }
        ) {
            Text(
                modifier = Modifier.padding(TempoTheme.dimensions.spaceM),
                text = routine.name,
                style = TempoTheme.typography.h2
            )
        }
    }
}

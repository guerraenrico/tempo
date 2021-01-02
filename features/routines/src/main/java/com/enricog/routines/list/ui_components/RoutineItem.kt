package com.enricog.routines.list.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Routine
import com.enricog.ui_components.common.surface.ListItemSurface
import com.enricog.ui_components.resources.dimensions

internal const val RoutineItemTestTag = "RoutineItemTestTag"

@Composable
internal fun RoutineItem(
    routine: Routine,
    onClick: (Routine) -> Unit,
    modifier: Modifier
) {
    ListItemSurface(
        modifier = modifier
            .testTag(RoutineItemTestTag)
            .clickable { onClick(routine) }
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.dimensions.spaceM),
            text = routine.name,
            style = MaterialTheme.typography.body1
        )
    }
}
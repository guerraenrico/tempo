package com.enricog.features.routines.list.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.ID
import com.enricog.features.routines.list.models.RoutinesViewState.Data.Routine
import com.enricog.features.routines.ui_components.DeletableListItem
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val RoutineItemTestTag = "RoutineItemTestTag"

@Composable
internal fun RoutineItem(
    modifier: Modifier = Modifier,
    routine: Routine,
    onClick: (ID) -> Unit,
    onDelete: (ID) -> Unit
) {
    DeletableListItem(
        modifier = modifier
            .testTag(RoutineItemTestTag),
        onDelete = { onDelete(routine.id) }
    ) {
        Box(modifier = Modifier.clickable { onClick(routine.id) }) {
            TempoText(
                modifier = Modifier.padding(TempoTheme.dimensions.spaceM),
                text = routine.name,
                style = TempoTheme.typography.h2
            )
        }
    }
}

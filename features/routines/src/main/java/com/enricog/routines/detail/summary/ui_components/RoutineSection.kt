package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import com.enricog.entities.routines.Routine
import com.enricog.routines.R
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize
import com.enricog.ui_components.resources.dimensions

internal const val RoutineSectionTestTag = "RoutineSectionTestTag"

@Composable
internal fun RoutineSection(
    routine: Routine,
    onEditRoutine: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .testTag(RoutineSectionTestTag)
            .fillMaxWidth()
            .padding(horizontal = MaterialTheme.dimensions.spaceM),
    ) {
        val (label, buttonEdit) = createRefs()
        Text(
            modifier = Modifier.constrainAs(label) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(buttonEdit.bottom)
                end.linkTo(buttonEdit.start)
                width = Dimension.fillToConstraints
            },
            text = routine.name,
            style = MaterialTheme.typography.h1
        )

        TempoIconButton(
            modifier = Modifier.constrainAs(buttonEdit) {
                top.linkTo(parent.top)
                start.linkTo(label.end)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
                baseline.linkTo(label.baseline)
            },
            onClick = onEditRoutine,
            size = TempoIconButtonSize.Small,
            icon = vectorResource(R.drawable.ic_routine_edit),
            color = TempoButtonColor.TransparentSecondary,
            drawShadow = false
        )
    }
}
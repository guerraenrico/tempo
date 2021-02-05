package com.enricog.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import com.enricog.routines.R
import com.enricog.ui_components.common.button.TempoButtonColor
import com.enricog.ui_components.common.button.TempoIconButton
import com.enricog.ui_components.common.button.TempoIconButtonSize

internal const val RoutineSectionTestTag = "RoutineSectionTestTag"

@Composable
internal fun RoutineSection(
    routineName: String,
    onEditRoutine: () -> Unit
) {
    ConstraintLayout(
        modifier = Modifier
            .testTag(RoutineSectionTestTag)
            .fillMaxWidth()
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
            text = routineName,
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
            drawShadow = false,
            contentDescription = stringResource(R.string.content_description_button_edit_routine)
        )
    }
}

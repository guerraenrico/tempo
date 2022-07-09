package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.features.routines.R
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.button.icon.TempoIconButtonSize
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

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
        TempoText(
            modifier = Modifier.constrainAs(label) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(buttonEdit.bottom)
                end.linkTo(buttonEdit.start)
                width = Dimension.fillToConstraints
            },
            text = routineName,
            style = TempoTheme.typography.h1
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
            icon = painterResource(R.drawable.ic_routine_edit),
            color = TempoButtonColor.TransparentSecondary,
            drawShadow = false,
            contentDescription = stringResource(R.string.content_description_button_edit_routine)
        )
    }
}

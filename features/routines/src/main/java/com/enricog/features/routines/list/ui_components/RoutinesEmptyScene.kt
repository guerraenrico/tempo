package com.enricog.features.routines.list.ui_components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.features.routines.R
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.layout.message.TempoLayoutMessage

internal const val RoutinesEmptySceneTestTag = "RoutinesEmptySceneTestTag"

@Composable
internal fun RoutinesEmptyScene(onCreateSegment: () -> Unit) {
    TempoLayoutMessage(
        modifier = Modifier.testTag(RoutinesEmptySceneTestTag),
        title = stringResource(R.string.label_no_routines_title),
        description = stringResource(R.string.label_no_routines_description),
        button = {
            TempoButton(
                onClick = onCreateSegment,
                text = stringResource(R.string.button_create_routine),
                color = TempoButtonColor.Accent,
                iconContentDescription = stringResource(R.string.content_description_button_create_routine)
            )
        }
    )
}

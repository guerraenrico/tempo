package com.enricog.features.routines.list.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.enricog.features.routines.R
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.theme.TempoTheme

internal const val EmptySceneTestTag = "EmptySceneTestTag"

@Composable
internal fun EmptyScene(onCreateSegmentClick: () -> Unit) {
    Column(
        modifier = Modifier
            .testTag(EmptySceneTestTag)
            .fillMaxSize()
            .padding(TempoTheme.dimensions.spaceM),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.description_no_routines),
            style = TempoTheme.typography.h3,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(40.dp))
        TempoButton(
            onClick = onCreateSegmentClick,
            text = stringResource(R.string.button_create_routine),
            color = TempoButtonColor.Accent,
            contentDescription = stringResource(R.string.content_description_button_create_routine)
        )
    }
}

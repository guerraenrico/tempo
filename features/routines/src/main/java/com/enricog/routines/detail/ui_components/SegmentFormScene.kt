package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Segment
import com.enricog.ui_components.common.textField.TempoTextField
import com.enricog.ui_components.resources.dimensions

internal const val SegmentFormSceneTestTag = "SegmentFormSceneTestTag"

@Composable
private fun SegmentFormScene(segment: Segment) {
    Column(
        modifier = Modifier
            .testTag(SegmentFormSceneTestTag)
            .padding(MaterialTheme.dimensions.spaceM)
    ) {
    }
}
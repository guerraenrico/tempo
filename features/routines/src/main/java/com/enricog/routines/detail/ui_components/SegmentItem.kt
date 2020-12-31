package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.entities.routines.Segment
import com.enricog.ui_components.resources.dimensions

internal const val SegmentItemTestTag = "SegmentItemTestTag"

@Composable
internal fun SegmentItem(segment: Segment, onClick: (Segment) -> Unit) {
    Surface(
        modifier = Modifier
            .testTag(SegmentItemTestTag)
            .fillMaxWidth()
            .clickable { onClick(segment) }
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.dimensions.spaceM),
            text = segment.name,
            style = MaterialTheme.typography.body1
        )
    }
}


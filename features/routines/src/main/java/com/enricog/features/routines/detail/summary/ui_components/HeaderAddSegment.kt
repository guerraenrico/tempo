package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.enricog.features.routines.R
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.theme.TempoTheme

internal const val HeaderAddSegmentButtonTestTag = "HeaderAddSegmentButton"

@Composable
internal fun HeaderAddSegment(
    modifier: Modifier = Modifier,
    onAddSegmentClick: () -> Unit
) {
    val enabled = true
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(TempoTheme.dimensions.spaceS)
    ) {
        TempoButton(
            onClick = onAddSegmentClick,
            icon = painterResource(id = R.drawable.ic_add),
            contentDescription = stringResource(R.string.content_description_button_add_segment),
            text = stringResource(R.string.button_add_segment),
            enabled = enabled,
            modifier = Modifier
                .testTag(HeaderAddSegmentButtonTestTag)
                .align(Alignment.Center),
        )
    }
}
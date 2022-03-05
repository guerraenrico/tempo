package com.enricog.features.routines.detail.summary.ui_components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.features.routines.R
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.white

internal const val HeaderAddSegmentButtonTestTag = "HeaderAddSegmentButton"

private val buttonTypography = TextStyle(
    fontWeight = FontWeight.Bold,
    color = white,
    fontSize = 14.sp
)

@Composable
internal fun HeaderAddSegment(
    modifier: Modifier = Modifier,
    onAddSegmentClick: () -> Unit
) {
    val color = TempoButtonColor.Normal
    val enabled = true
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(TempoTheme.dimensions.spaceS)
    ) {
        Button(
            onClick = onAddSegmentClick,
            modifier = Modifier
                .testTag(HeaderAddSegmentButtonTestTag)
                .align(Alignment.Center),
            enabled = enabled,
            colors = color
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add),
                tint = TempoButtonColor.Normal.contentColor(true).value,
                contentDescription = stringResource(R.string.content_description_button_add_segment),
                modifier = Modifier.size(12.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = stringResource(R.string.button_add_segment),
                style = buttonTypography,
                color = color.contentColor(enabled).value
            )
        }
    }
}
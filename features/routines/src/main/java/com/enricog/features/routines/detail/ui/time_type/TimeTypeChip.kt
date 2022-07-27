package com.enricog.features.routines.detail.ui.time_type

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.enricog.data.routines.api.entities.TimeType
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.white

internal const val TimeTypeChipTestTag = "TimeTypeChipTestTag"
private val chipShape = RoundedCornerShape(percent = 50)

@Composable
internal fun TimeTypeChip(
    value: TimeType,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .testTag(TimeTypeChipTestTag)
            .clip(chipShape)
            .background(color = value.color(), shape = chipShape),
        contentAlignment = Alignment.Center
    ) {
        TempoText(
            text = value.text(),
            style = TextStyle(
                color = white,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
            ),
            modifier = Modifier.padding(
                horizontal = TempoTheme.dimensions.spaceM,
                vertical = TempoTheme.dimensions.spaceXS
            )
        )
    }
}

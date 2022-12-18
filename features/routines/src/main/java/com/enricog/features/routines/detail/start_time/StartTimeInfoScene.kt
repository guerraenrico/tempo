package com.enricog.features.routines.detail.start_time

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.features.routines.R
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val StartTimeInfoSceneTestTag = "StartTimeInfoSceneTestTag"

internal const val StartTimeInfoTitleTestTag = "StartTimeInfoTitleTestTag"
internal const val StartTimeInfoDescriptionTestTag = "StartTimeInfoDescriptionTestTag"

@Composable
internal fun StartTimeInfoScene() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(TempoTheme.dimensions.spaceM)
            .testTag(StartTimeInfoSceneTestTag)
    ) {
        TempoText(
            modifier = Modifier.testTag(StartTimeInfoTitleTestTag),
            text = stringResource(id = R.string.label_routine_start_time_info_title),
            style = TempoTheme.typography.h1
        )
        TempoText(
            modifier = Modifier.testTag(StartTimeInfoDescriptionTestTag),
            text = stringResource(id = R.string.label_routine_start_time_info_description),
            style = TempoTheme.typography.body1
        )
    }
}

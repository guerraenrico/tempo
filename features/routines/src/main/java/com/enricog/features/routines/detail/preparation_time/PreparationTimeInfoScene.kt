package com.enricog.features.routines.detail.preparation_time

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import com.enricog.features.routines.R
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

internal const val PreparationTimeInfoSceneTestTag = "PreparationTimeInfoSceneTestTag"

internal const val PreparationTimeInfoTitleTestTag = "PreparationTimeInfoTitleTestTag"
internal const val PreparationTimeInfoDescriptionTestTag = "PreparationTimeInfoDescriptionTestTag"

@Composable
internal fun PreparationTimeInfoScene() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .windowInsetsPadding(WindowInsets.navigationBars)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(TempoTheme.dimensions.spaceM)
            .testTag(PreparationTimeInfoSceneTestTag)
    ) {
        TempoText(
            modifier = Modifier.testTag(PreparationTimeInfoTitleTestTag),
            text = stringResource(id = R.string.label_routine_preparation_time_info_title),
            style = TempoTheme.typography.h1
        )
        TempoText(
            modifier = Modifier.testTag(PreparationTimeInfoDescriptionTestTag),
            text = stringResource(id = R.string.label_routine_preparation_time_info_description),
            style = TempoTheme.typography.body1
        )
    }
}

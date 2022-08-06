package com.enricog.features.routines.detail.start_time

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.enricog.features.routines.R
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun StartTimeInfoScreen() {

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(TempoTheme.dimensions.spaceM)
    ) {

        TempoText(
            text = stringResource(id = R.string.label_routine_start_time_info_title),
            style = TempoTheme.typography.h1
        )

        TempoText(
            text = stringResource(id = R.string.label_routine_start_time_info_description),
            style = TempoTheme.typography.body1
        )

    }
}

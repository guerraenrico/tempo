package com.enricog.ui.components.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.enricog.entities.Seconds
import com.enricog.ui.components.R

@Composable
fun Seconds.format(): String {
    return buildString {
        if (minutes > 0) {
            append("$minutes${stringResource(id = R.string.label_minutes_acronym)} ")
        }
        if (secondsRemainingInMinute > 0) {
            append("$secondsRemainingInMinute${stringResource(id = R.string.label_seconds_acronym)}")
        }
    }
}

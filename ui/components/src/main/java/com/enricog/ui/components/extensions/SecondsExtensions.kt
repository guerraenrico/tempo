package com.enricog.ui.components.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.enricog.entities.Seconds
import com.enricog.ui.components.R

@Composable
fun Seconds.format(): String {
    return buildString {
        val (m, s) = inMinutes
        if (m > 0) {
            append("$m${stringResource(id = R.string.label_minutes_acronym)}")
        }
        if (s > 0) {
            if (m > 0) {
                append(" ")
            }
            append("$s${stringResource(id = R.string.label_seconds_acronym)}")
        }
    }
}

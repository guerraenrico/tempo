package com.enricog.core.compose.api.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.res.stringResource

@Composable
@ReadOnlyComposable
fun stringResourceOrNull(@StringRes id: Int?): String? {
    return id?.let { stringResource(id = id) }
}

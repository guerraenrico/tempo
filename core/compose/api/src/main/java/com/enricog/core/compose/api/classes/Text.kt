package com.enricog.core.compose.api.classes

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.res.stringResource
import kotlin.String as KString

sealed class Text {
    @Immutable
    data class Resource(@StringRes val resId: Int) : Text()

    @Immutable
    data class String(val value: KString) : Text()

    @Composable
    fun resolveString(): KString {
        return when (this) {
            is Resource -> stringResource(id = resId)
            is String -> value
        }
    }
}
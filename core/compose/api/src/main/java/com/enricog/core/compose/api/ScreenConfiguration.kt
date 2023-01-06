package com.enricog.core.compose.api

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalConfiguration

object ScreenConfiguration {

    val orientation: Orientation
        @ReadOnlyComposable
        @Composable
        get() = when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> Orientation.PORTRAIT
            Configuration.ORIENTATION_LANDSCAPE -> Orientation.LANDSCAPE
            else -> Orientation.PORTRAIT
        }

    enum class Orientation {
        PORTRAIT, LANDSCAPE
    }
}


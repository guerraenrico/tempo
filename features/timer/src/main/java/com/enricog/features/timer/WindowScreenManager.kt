package com.enricog.features.timer

import kotlinx.coroutines.flow.Flow

interface WindowScreenManager {
    val keepScreenOn: Flow<Boolean>

    fun toggleKeepScreenOnFlag(enable: Boolean)
}

package com.enricog.features.timer.fakes

import com.enricog.features.timer.WindowScreenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeWindowScreenManager : WindowScreenManager {

    private val flag = MutableStateFlow(false)

    override val keepScreenOn: Flow<Boolean> = flag.asStateFlow()

    override fun toggleKeepScreenOnFlag(enable: Boolean) {
        flag.value = enable
    }
}
package com.enricog.tempo.managers

import com.enricog.timer.WindowScreenManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

internal class WindowScreenManagerImpl @Inject constructor(
) : WindowScreenManager {

    private val flag = MutableStateFlow(false)

    override val keepScreenOn: Flow<Boolean>
        get() = flag

    override fun toggleKeepScreenOnFlag(enable: Boolean) {
        flag.value = enable
    }

}
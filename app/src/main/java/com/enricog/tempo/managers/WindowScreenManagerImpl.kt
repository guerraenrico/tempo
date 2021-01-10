package com.enricog.tempo.managers

import android.app.Activity
import com.enricog.timer.WindowScreenManager
import javax.inject.Inject

internal class WindowScreenManagerImpl @Inject constructor(
    private val activity: Activity
) : WindowScreenManager {

    override fun toggleKeepScreenOnFlag(enable: Boolean) {
        activity.runOnUiThread {
            if (enable) {
                activity.window.addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            } else {
                activity.window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            }
        }
    }

}
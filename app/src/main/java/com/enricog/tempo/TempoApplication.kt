package com.enricog.tempo

import android.app.Application
import com.enricog.core.logger.api.TempoLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TempoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TempoLogger.install(isDebug = BuildConfig.DEBUG)
    }
}

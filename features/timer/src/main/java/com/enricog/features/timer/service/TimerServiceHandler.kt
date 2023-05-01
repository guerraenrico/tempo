package com.enricog.features.timer.service

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class TimerServiceHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun startService() {
        Intent(context, TimerService::class.java).also { intent ->
            context.startForegroundService(intent)
        }
    }

    fun stopService() {
        Intent(context, TimerService::class.java).also { intent ->
            context.stopService(intent)
        }
    }
}
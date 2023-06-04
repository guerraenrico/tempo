package com.enricog.features.timer.service

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class TimerServiceHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TimerServiceHandler {

    override fun start() {
        Intent(context, TimerService::class.java).also { intent ->
            context.startForegroundService(intent)
        }
    }

    override fun stop() {
        Intent(context, TimerService::class.java).also { intent ->
            context.stopService(intent)
        }
    }
}
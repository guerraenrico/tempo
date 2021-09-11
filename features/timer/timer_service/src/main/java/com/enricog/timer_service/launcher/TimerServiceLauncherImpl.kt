package com.enricog.timer_service.launcher

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.enricog.timer_service.TimerService
import com.enricog.timer_service.TimerServiceActions
import com.enricog.timer_service.TimerWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TimerServiceLauncherImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : TimerServiceLauncher {

    override fun launchWorker() {
        val timerWorker = OneTimeWorkRequestBuilder<TimerWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiresDeviceIdle(false)
                    .setRequiresCharging(false)
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .setRequiresBatteryNotLow(false)
                    .build()
            )
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            TimerWorker.NAME,
            ExistingWorkPolicy.KEEP,
            timerWorker
        )
    }

    override fun launchService() {
        Intent(context, TimerService::class.java).also { intent ->
            intent.action = TimerServiceActions.START.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }
}
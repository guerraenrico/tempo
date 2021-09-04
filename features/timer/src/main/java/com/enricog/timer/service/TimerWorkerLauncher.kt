package com.enricog.timer.service

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class TimerWorkerLauncher @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun launch() {
        val timerWorker = OneTimeWorkRequestBuilder<TimerWorker>()
            .setConstraints(Constraints.Builder()
                .build())
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            TimerWorker.NAME,
            ExistingWorkPolicy.KEEP,
            timerWorker
        )
    }
}
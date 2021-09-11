package com.enricog.timer_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.enricog.timer_service.controller.TimerControllerImpl
import com.enricog.timer_service.models.TimerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect

@HiltWorker
internal class TimerWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val timerController: TimerControllerImpl
) : CoroutineWorker(context, workerParams) {

    private val channelId = context.getString(R.string.timer_service_notification_channel_id)
    private val channelName = context.getString(R.string.timer_service_notification_channel_name)
    private val channelDescription =
        context.getString(R.string.timer_service_notification_channel_description)
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    override suspend fun doWork(): Result {
//        val wakeLock = (context.getSystemService(Context.POWER_SERVICE) as PowerManager)
//            .newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "TimerWorker::lock")
//        wakeLock.acquire(10*60*1000L /*10 minutes*/)

        try {
            setForeground(createForegroundInfo(TimerState.Idle))
            timerController.state
                .collect { newState ->
                    notificationManager.notify(NOTIFICATION_ID, createNotification(newState))
                }
        } finally {
//            wakeLock.release()
        }

        return Result.success()
    }

    private fun createForegroundInfo(state: TimerState): ForegroundInfo {
        val notification = createNotification(state)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK
            )
        } else {
            ForegroundInfo(NOTIFICATION_ID, notification)
        }
    }

    private fun createNotification(state: TimerState): Notification {
        val title = when (state) {
            TimerState.Idle -> applicationContext.getString(R.string.timer_service_title_segment_step_type_starting)
            is TimerState.Counting -> state.runningSegment.name
        }
        val content = when (state) {
            TimerState.Idle -> ""
            is TimerState.Counting -> state.step.count.seconds.toString()
        }

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(id)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }
        return NotificationCompat.Builder(applicationContext, channelId)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(content)
            .addAction(0, "Stop", intent)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_HIGH
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NAME = "TimerWorker"
        private val NOTIFICATION_ID = R.id.timer_service_notification_id
    }
}
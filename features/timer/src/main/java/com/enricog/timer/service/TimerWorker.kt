package com.enricog.timer.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.enricog.timer.R
import com.enricog.timer.models.TimerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.collect

@HiltWorker
internal class TimerWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val routineRunner: RoutineRunner
) : CoroutineWorker(context, workerParams) {

    private val channelId = context.getString(R.string.timer_notification_channel_id)
    private val channelName = context.getString(R.string.timer_notification_channel_name)
    private val channelDescription =
        context.getString(R.string.timer_notification_channel_description)
    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        print("")
    }

    override suspend fun doWork(): Result {
        routineRunner.state
            .collect { newState ->
                val foregroundInfo = createForegroundInfo(newState)
                setForeground(foregroundInfo)
            }
        return Result.success()
    }

    private fun createForegroundInfo(state: TimerState): ForegroundInfo {
        val id = applicationContext.getString(R.string.timer_notification_id)
        val title = when (state) {
            TimerState.Idle -> applicationContext.getString(R.string.title_segment_step_type_starting)
            is TimerState.Counting -> state.runningSegment.name
        }
        val content = when (state) {
            TimerState.Idle -> ""
            is TimerState.Counting -> state.step.count.seconds.toString()
        }

        val intent = WorkManager.getInstance(applicationContext)
            .createCancelPendingIntent(getId())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        val notification = NotificationCompat.Builder(applicationContext, id)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(content)
            .setOngoing(true)
            .addAction(0, "Stop", intent)
            .build()

        return ForegroundInfo(R.string.timer_notification_id, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription
        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val NAME = "TimerWorker"
    }
}
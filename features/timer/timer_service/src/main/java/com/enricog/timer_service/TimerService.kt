package com.enricog.timer_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.MediaBrowserCompat
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.media.MediaBrowserServiceCompat
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.core.coroutine.scope.ApplicationCoroutineScope
import com.enricog.timer_service.controller.TimerController
import com.enricog.timer_service.models.TimerState
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
internal class TimerService : Service() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    @ApplicationCoroutineScope
    lateinit var scope: CoroutineScope

    @Inject
    lateinit var timerController: TimerController

    private var isStarted = false

    private val channelId by lazy { context.getString(R.string.timer_service_notification_channel_id) }
    private val channelName by lazy { context.getString(R.string.timer_service_notification_channel_name) }
    private val channelDescription by lazy { context.getString(R.string.timer_service_notification_channel_description) }

    private val notificationManager by lazy {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private var serviceJob by autoCancelableJob()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TimerServiceActions.START.name -> start()
            TimerServiceActions.STOP.name -> stop()
            else -> throw IllegalArgumentException("${intent?.action} is not a valid TimerService action")
        }
        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        val notification = createNotification(TimerState.Idle)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_NONE)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        start()
    }

    override fun onDestroy() {
        super.onDestroy()
        stop()
    }

    private fun start() {
        if (isStarted) return
        isStarted = true
        serviceJob = scope.launch {
            timerController.state.collect { state ->
                val notification = createNotification(state)
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel()
        }

        return NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setOnlyAlertOnce(true)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(content)
            .addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.media_session_service_notification_ic_play,
                    "Stop",
                    buildPendingIntent(context, TimerServiceActions.STOP.name)
                ).build()
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setGroup(NOTIFICATION_GROUP)
            .build()
    }

    private fun buildPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, TimerService::class.java)
        intent.action = action
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            PendingIntent.getForegroundService(
                context,
                0,
                intent,
                PendingIntent.FLAG_CANCEL_CURRENT
            )
        } else {
            PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_LOW
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription
        notificationManager.createNotificationChannel(channel)
    }

    private fun stop() {
        isStarted = false
        serviceJob?.cancel()
    }

    companion object {
        private val NOTIFICATION_ID = R.id.timer_service_notification_id
        private const val NOTIFICATION_GROUP = "com.enricog.tempo"
    }
}

internal enum class TimerServiceActions {
    START, STOP,
}
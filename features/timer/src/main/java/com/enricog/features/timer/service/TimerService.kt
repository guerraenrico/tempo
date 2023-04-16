package com.enricog.features.timer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.features.timer.R
import com.enricog.features.timer.TimerController
import com.enricog.features.timer.models.TimerState
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class TimerService : Service() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var timerController: TimerController

    private val scope = CoroutineScope(Dispatchers.Default)
    private var stateJob by autoCancelableJob()

    private var isStarted = false

    private val channelId by lazy { context.getString(R.string.timer_service_notification_channel_id) }
    private val channelName by lazy { context.getString(R.string.timer_service_notification_channel_name) }
    private val channelDescription by lazy { context.getString(R.string.timer_service_notification_channel_description) }

    private val notificationManager by lazy {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

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
        val notification = createNotification(count = 0)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
        start()
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun start() {
        if (isStarted) return
        isStarted = true

        stateJob = timerController.state
            .filterIsInstance<TimerState.Counting>()
            .onEach {
                delay(10)
                val notification = createNotification(count = it.runningStep.count.seconds.value)
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
            .launchIn(scope)
    }

    private fun createNotification(count: Long): Notification {

        createChannel()

        return NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setOnlyAlertOnce(true)
            .setContentTitle("Tempo time")
            .setTicker("Tempo time")
            .setContentText("count: $count")
            .addAction(
                NotificationCompat.Action.Builder(
                    R.drawable.ic_timer_stop,
                    "Stop",
                    buildPendingIntent(context, TimerServiceActions.STOP.name)
                ).build()
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(NOTIFICATION_GROUP)
            .build()
    }

    private fun buildPendingIntent(context: Context, action: String): PendingIntent {
        val intent = Intent(context, TimerService::class.java)
        intent.action = action
        return PendingIntent.getForegroundService(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createChannel() {
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription
//        channel.setSound(null, null)
//        channel.setShowBadge(true)
        notificationManager.createNotificationChannel(channel)
    }

    private fun stop() {
        isStarted = false
        timerController.onPlay()
    }

    private companion object {
        val NOTIFICATION_ID = R.id.timer_service_notification_id
        const val NOTIFICATION_GROUP = "com.enricog.tempo.debug"
    }
}

internal enum class TimerServiceActions {
    START, STOP,
}
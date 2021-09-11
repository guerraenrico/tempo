package com.enricog.timer_service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.enricog.core.coroutine.job.autoCancelableJob
import com.enricog.core.coroutine.scope.ApplicationCoroutineScope
import com.enricog.timer_service.controller.TimerControllerImpl
import com.enricog.timer_service.models.TimerState
import dagger.hilt.android.AndroidEntryPoint
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
    lateinit var timerController: TimerControllerImpl

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
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setCategory(NotificationCompat.CATEGORY_STOPWATCH)
            .setGroup(NOTIFICATION_GROUP)
            .build()
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
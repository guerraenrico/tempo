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
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.features.timer.R
import com.enricog.features.timer.TimerController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
internal class TimerService : Service() {

    @Inject
    @ApplicationContext
    lateinit var context: Context

    @Inject
    lateinit var timerController: TimerController

    @Inject
    lateinit var stateConverter: TimerServiceStateConverter

    @Inject
    lateinit var dispatchers: CoroutineDispatchers

    private val scope by lazy {
        CoroutineScope(SupervisorJob() + dispatchers.main)
    }
    private var stateJob by autoCancelableJob()

    private var isForegroundServiceSet = false
    private var isNotificationChannelCreated = false
    private var isStarted = false

    private val channelId by lazy { context.getString(R.string.timer_service_notification_channel_id) }
    private val channelName by lazy { context.getString(R.string.timer_service_notification_channel_name) }
    private val channelDescription by lazy { context.getString(R.string.timer_service_notification_channel_description) }

    private val notificationManager by lazy {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        stateJob = timerController.state
            .map(stateConverter::convert)
            .distinctUntilChanged()
            .filterIsInstance<TimerServiceViewState.Counting>()
            .flowOn(dispatchers.cpu)
            .onEach {
                val notification = createNotification(state = it)
                startForegroundServiceIfNeeded(notification)
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
            .flowOn(dispatchers.main)
            .launchIn(scope)
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private fun startForegroundServiceIfNeeded(notification: Notification) {
        if (isForegroundServiceSet) return
        isForegroundServiceSet = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(NOTIFICATION_ID, notification, ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK)
        } else {
            startForeground(NOTIFICATION_ID, notification)
        }
    }

    private fun createChannel() {
        if (isNotificationChannelCreated) return
        isNotificationChannelCreated = true

        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        channel.description = channelDescription
//        channel.setSound(null, null)
//        channel.setShowBadge(true)
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(state: TimerServiceViewState.Counting): Notification {

        createChannel()

        return NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setSilent(true)
            .setColorized(true)
            .setColor(state.clockBackground.background.toArgb())
            .setCustomBigContentView(buildView(state))
            .setCustomContentView(buildView(state))
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

    private fun buildView(state: TimerServiceViewState.Counting): RemoteViews {
        val views = RemoteViews(context.packageName, R.layout.notification_timer_content)

        views.setTextColor(R.id.notification_timer_step_name, state.clockOnBackgroundColor.toArgb())
        views.setTextViewText(R.id.notification_timer_step_name, context.getString(state.stepTitleId))

        views.setTextColor(R.id.notification_timer_segment_name, state.clockOnBackgroundColor.toArgb())
        views.setTextViewText(R.id.notification_timer_segment_name, state.segmentName)

        views.setTextColor(R.id.notification_timer_count, state.clockOnBackgroundColor.toArgb())
        views.setTextViewText(R.id.notification_timer_count, state.time)

        return views
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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TimerServiceActions.START.name -> start()
            TimerServiceActions.STOP.name -> stop()
            else -> throw IllegalArgumentException("${intent?.action} is not a valid TimerService action")
        }
        return START_NOT_STICKY
    }

    private fun start() {
        if (isStarted) return
        isStarted = true

        // TODO should handle here the start/stop action in the notification?
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
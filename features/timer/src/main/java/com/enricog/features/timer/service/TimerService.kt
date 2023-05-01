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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.runningFold
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

    private val channelId by lazy { context.getString(R.string.timer_service_notification_channel_id) }
    private val channelName by lazy { context.getString(R.string.timer_service_notification_channel_name) }
    private val channelDescription by lazy { context.getString(R.string.timer_service_notification_channel_description) }

    private val notificationManager by lazy(LazyThreadSafetyMode.NONE) {
        context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    private val contentView by lazy(LazyThreadSafetyMode.NONE) {
        RemoteViews(context.packageName, R.layout.notification_timer_content)
    }

    private val bigContentView by lazy(LazyThreadSafetyMode.NONE) {
        RemoteViews(context.packageName, R.layout.notification_timer_big_content)
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
            .history()
            .filterNotNull()
            .onEach {
                val notification = createNotification(stateHistory = it)
                startForegroundServiceIfNeeded(notification)
                notificationManager.notify(NOTIFICATION_ID, notification)
            }
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
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification(stateHistory: StateHistory): Notification {

        createChannel()

        return NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setSilent(true)
            .setColorized(true)
            .setColor(stateHistory.current.clockBackground.background.toArgb())
            .setCustomContentView(buildContentView(stateHistory))
            .setCustomBigContentView(buildBigContentView(stateHistory))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(NOTIFICATION_GROUP)
            .build()
    }

    private fun buildContentView(stateHistory: StateHistory): RemoteViews = contentView.apply {
        if (stateHistory.prev?.clockOnBackgroundColor != stateHistory.current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_step_name, stateHistory.current.clockOnBackgroundColor.toArgb())
        }
        if (stateHistory.prev?.stepTitleId != stateHistory.current.stepTitleId) {
            setTextViewText(R.id.notification_timer_step_name, context.getString(stateHistory.current.stepTitleId))
        }
        if (stateHistory.prev?.clockOnBackgroundColor != stateHistory.current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_segment_name, stateHistory.current.clockOnBackgroundColor.toArgb())
            setTextColor(R.id.notification_timer_count, stateHistory.current.clockOnBackgroundColor.toArgb())
        }
        if (stateHistory.prev?.segmentName != stateHistory.current.segmentName) {
            setTextViewText(R.id.notification_timer_segment_name, stateHistory.current.segmentName)
        }
        setTextViewText(R.id.notification_timer_count, stateHistory.current.time)
    }

    private fun buildBigContentView(stateHistory: StateHistory): RemoteViews = bigContentView.apply {
        if (stateHistory.prev?.clockOnBackgroundColor != stateHistory.current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_step_name, stateHistory.current.clockOnBackgroundColor.toArgb())
        }
        if (stateHistory.prev?.stepTitleId != stateHistory.current.stepTitleId) {
            setTextViewText(R.id.notification_timer_step_name, context.getString(stateHistory.current.stepTitleId))
        }
        if (stateHistory.prev?.clockOnBackgroundColor != stateHistory.current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_segment_name, stateHistory.current.clockOnBackgroundColor.toArgb())
            setTextColor(R.id.notification_timer_count, stateHistory.current.clockOnBackgroundColor.toArgb())
        }
        if (stateHistory.prev?.segmentName != stateHistory.current.segmentName) {
            setTextViewText(R.id.notification_timer_segment_name, stateHistory.current.segmentName)
        }
        setTextViewText(R.id.notification_timer_count, stateHistory.current.time)

        if (stateHistory.prev?.timerActions != stateHistory.current.timerActions) {
            setContentDescription(
                R.id.notification_timer_play_button,
                getString(stateHistory.current.timerActions.play.contentDescriptionResId)
            )
            setInt(
                R.id.notification_timer_play_button,
                "setImageResource",
                stateHistory.current.timerActions.play.iconResId
            )
            setOnClickPendingIntent(
                R.id.notification_timer_play_button,
                TimerServiceActions.PLAY.getPendingIntent(context)
            )
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TimerServiceActions.PLAY.name -> timerController.onPlay()
        }
        return START_NOT_STICKY
    }

    private companion object {
        val NOTIFICATION_ID = R.id.timer_service_notification_id
        const val NOTIFICATION_GROUP = "com.enricog.tempo.debug"
    }

    private data class StateHistory(
        val prev: TimerServiceViewState.Counting?,
        val current: TimerServiceViewState.Counting
    )

    private fun Flow<TimerServiceViewState.Counting>.history(): Flow<StateHistory?> =
        runningFold(
            initial = null as StateHistory?,
            operation = { acc, new -> StateHistory(prev = acc?.current, current = new) }
        )
}

private enum class TimerServiceActions {
    PLAY;

    fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, TimerService::class.java)
        intent.action = name
        return PendingIntent.getForegroundService(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
    }
}
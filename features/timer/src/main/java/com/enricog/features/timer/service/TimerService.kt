package com.enricog.features.timer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.view.View
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.graphics.toColor
import com.enricog.core.coroutines.dispatchers.CoroutineDispatchers
import com.enricog.core.coroutines.job.autoCancelableJob
import com.enricog.features.timer.BuildConfig
import com.enricog.features.timer.R
import com.enricog.features.timer.TimerController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
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

    private val completedContentView by lazy(LazyThreadSafetyMode.NONE) {
        RemoteViews(context.packageName, R.layout.notification_timer_completed_content)
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
            .filter { it is TimerServiceViewState.Counting || it is TimerServiceViewState.Completed }
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
        val color = if (stateHistory.current is TimerServiceViewState.Counting) {
            stateHistory.current.clockBackground.background.argb
        } else {
            Color.parseColor("#1A1D20").toColor().toArgb()
        }
        val contentView = if (stateHistory.current is TimerServiceViewState.Counting) {
            buildContentView(prev = stateHistory.prev?.takeIfInstance(), current = stateHistory.current)
        } else {
            buildCompletedContent()
        }
        val bigContentView = if (stateHistory.current is TimerServiceViewState.Counting) {
            buildBigContentView(prev = stateHistory.prev?.takeIfInstance(), current = stateHistory.current)
        } else {
            buildCompletedContent()
        }

        return NotificationCompat.Builder(applicationContext, channelId)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSmallIcon(R.drawable.ic_notification)
            .setSilent(true)
            .setColorized(true)
            .setColor(color)
            .setCustomContentView(contentView)
            .setCustomBigContentView(bigContentView)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setGroup(BuildConfig.NOTIFICATION_GROUP)
            .build()
    }

    private fun buildContentView(
        prev: TimerServiceViewState.Counting?,
        current: TimerServiceViewState.Counting
    ): RemoteViews = contentView.apply {
        if (prev?.clockOnBackgroundColor != current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_step_name, current.clockOnBackgroundColor.argb)
        }
        if (prev?.stepTitleId != current.stepTitleId) {
            setTextViewText(R.id.notification_timer_step_name, context.getString(current.stepTitleId))
        }
        if (prev?.clockOnBackgroundColor != current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_segment_name, current.clockOnBackgroundColor.argb)
            setTextColor(R.id.notification_timer_count, current.clockOnBackgroundColor.argb)
        }
        if (prev?.segmentName != current.segmentName || prev.segmentRoundText != current.segmentRoundText) {
            val text = buildString {
                append(current.segmentName)
                if (current.segmentRoundText != null) {
                    append(" (")
                    append(
                        getString(
                            current.segmentRoundText.labelId,
                            *current.segmentRoundText.formatArgs.toTypedArray()
                        )
                    )
                    append(")")
                }
            }
            setTextViewText(R.id.notification_timer_segment_name, text)
        }
        setTextViewText(R.id.notification_timer_count, current.time)
    }

    private fun buildBigContentView(
        prev: TimerServiceViewState.Counting?,
        current: TimerServiceViewState.Counting
    ): RemoteViews = bigContentView.apply {
        if (prev?.clockOnBackgroundColor != current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_step_name, current.clockOnBackgroundColor.argb)
        }
        if (prev?.stepTitleId != current.stepTitleId) {
            setTextViewText(R.id.notification_timer_step_name, context.getString(current.stepTitleId))
        }
        if (prev?.clockOnBackgroundColor != current.clockOnBackgroundColor) {
            setTextColor(R.id.notification_timer_segment_name, current.clockOnBackgroundColor.argb)
            setTextColor(R.id.notification_timer_count, current.clockOnBackgroundColor.argb)
            setTextColor(R.id.notification_timer_segment_round, current.clockOnBackgroundColor.argb)
        }
        if (prev?.segmentName != current.segmentName) {
            setTextViewText(R.id.notification_timer_segment_name, current.segmentName)
        }
        // TODO where to show the routine rounds?
        if (prev?.segmentRoundText != current.segmentRoundText) {
            if (current.segmentRoundText != null) {
                setViewVisibility(R.id.notification_timer_segment_round, View.VISIBLE)
                val text =
                    getString(current.segmentRoundText.labelId, *current.segmentRoundText.formatArgs.toTypedArray())
                setTextViewText(R.id.notification_timer_segment_round, text)
            } else {
                setViewVisibility(R.id.notification_timer_segment_round, View.GONE)
            }

        }
        setTextViewText(R.id.notification_timer_count, current.time)

        if (prev?.timerActions != current.timerActions) {
            setContentDescription(
                R.id.notification_timer_play_button,
                getString(current.timerActions.play.contentDescriptionResId)
            )
            setInt(
                R.id.notification_timer_play_button,
                "setImageResource",
                current.timerActions.play.iconResId
            )
            setOnClickPendingIntent(
                R.id.notification_timer_play_button,
                TimerServiceActions.PLAY.getPendingIntent(context)
            )
        }
    }

    private fun buildCompletedContent() = completedContentView.apply {
        setTextColor(R.id.notification_timer_completed_label, Color.parseColor("#FFFFFF").toColor().toArgb())
        setTextViewText(R.id.notification_timer_completed_label, context.getString(R.string.title_routine_completed))
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            TimerServiceActions.PLAY.name -> timerController.onPlay()
        }
        return START_NOT_STICKY
    }

    private data class StateHistory(
        val prev: TimerServiceViewState?,
        val current: TimerServiceViewState
    )

    private fun Flow<TimerServiceViewState>.history(): Flow<StateHistory?> =
        runningFold(
            initial = null as StateHistory?,
            operation = { acc, new -> StateHistory(prev = acc?.current, current = new) }
        )

    private val Long.argb: Int
        get() = Color.valueOf(this).toArgb()

    private inline fun <reified T : TimerServiceViewState> TimerServiceViewState.takeIfInstance(): T? {
        return if (this is T) this else null
    }

    private companion object {
        val NOTIFICATION_ID = R.id.timer_service_notification_id
    }
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
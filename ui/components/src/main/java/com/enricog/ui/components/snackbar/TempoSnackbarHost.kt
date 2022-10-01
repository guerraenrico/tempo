package com.enricog.ui.components.snackbar

import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

@Composable
fun rememberSnackbarHostState(
    snackbarHostState: TempoSnackbarHostState = TempoSnackbarHostState()
): TempoSnackbarHostState = remember { snackbarHostState }

@Composable
fun TempoSnackbarHost(
    modifier: Modifier = Modifier,
    state: TempoSnackbarHostState,
    content: @Composable () -> Unit
) {
    val data = state.snackbarData
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(key1 = data) {
        if (data != null) {
            val duration = TempoSnackbarHostDefaults.Duration.calculateRecommendedTimeoutMillis(
                hasAction = data.actionText != null,
                accessibilityManager = accessibilityManager
            )
            delay(duration)
            data.dismiss()
        }
    }

    val snackbar = @Composable {
        Crossfade(targetState = state.snackbarData) {
            if (it != null) {
                TempoSnackbar(snackbarData = it)
            }
        }
    }

    SubcomposeLayout(modifier = modifier) { constraints ->
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight

        val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        layout(width = layoutWidth, height = layoutHeight) {
            val contentPlaceable = subcompose(TempoSnackbarHostPlaceables.Content, content).map {
                it.measure(looseConstraints)
            }
            contentPlaceable.forEach { it.place(x = 0, y = 0) }

            val snackbarPlaceable = subcompose(TempoSnackbarHostPlaceables.Snackbar, snackbar).map {
                it.measure(looseConstraints)
            }
            val snackbarHeight = snackbarPlaceable.maxBy { it.height }.height
            val snackbarY = layoutHeight - snackbarHeight - 16.dp.roundToPx()
            snackbarPlaceable.forEach { it.place(x = 0, y = snackbarY) }
        }
    }
}

private enum class TempoSnackbarHostPlaceables {
    Snackbar, Content
}

interface TempoSnackbarData {
    val message: String
    val actionText: String?

    fun perform()

    fun dismiss()
}

@Stable
class TempoSnackbarHostState {

    private val mutex = Mutex()

    var snackbarData by mutableStateOf<TempoSnackbarData?>(null)
        private set


    suspend fun show(message: String, actionText: String? = null): TempoSnackbarEvent {
        return mutex.withLock {
            try {
                suspendCancellableCoroutine { continuation ->
                    snackbarData = TempoSnackbarDataImpl(
                        message = message,
                        actionText = actionText,
                        continuation = continuation
                    )
                }
            } finally {
                snackbarData = null
            }
        }
    }

    @Stable
    private class TempoSnackbarDataImpl(
        override val message: String,
        override val actionText: String?,
        private val continuation: CancellableContinuation<TempoSnackbarEvent>
    ) : TempoSnackbarData {

        override fun perform() {
            if (continuation.isActive) {
                continuation.resume(TempoSnackbarEvent.ActionPerformed)
            }
        }

        override fun dismiss() {
            if (continuation.isActive) {
                continuation.resume(TempoSnackbarEvent.Dismissed)
            }
        }

    }
}

enum class TempoSnackbarEvent {
    ActionPerformed, Dismissed
}

private fun Long.calculateRecommendedTimeoutMillis(
    hasAction: Boolean,
    accessibilityManager: AccessibilityManager?
): Long {
    if (accessibilityManager == null) {
        return this
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        originalTimeoutMillis = this,
        containsIcons = true,
        containsText = true,
        containsControls = hasAction
    )
}

private object TempoSnackbarHostDefaults {

    const val Duration = 4000L
}

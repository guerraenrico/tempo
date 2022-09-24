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
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.platform.LocalAccessibilityManager
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.resume

@Composable
fun rememberSnackbarHostState(
    snackbarHostState: TempoSnackbarHostState = TempoSnackbarHostState()
): TempoSnackbarHostState = remember {
    snackbarHostState
}

@Composable
fun TempoSnackbarHost(
    state: TempoSnackbarHostState,
    modifier: Modifier = Modifier
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

    Crossfade(targetState = state.snackbarData) {
        if (it != null) {
            TempoSnackbar(snackbarData = it, modifier = modifier)
        }
    }
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

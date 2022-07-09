package com.enricog.core.compose.api.modifiers.swipeable

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.FixedThreshold
import androidx.compose.material.ThresholdConfig
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.FractionalThreshold as MaterialFractionalThreshold
import androidx.compose.material.SwipeableState as MaterialSwipeableState
import androidx.compose.material.rememberSwipeableState as materialRememberSwipeableState
import androidx.compose.material.swipeable as materialSwipeable

typealias SwipeableState<T> = MaterialSwipeableState<T>

typealias FractionalThreshold = MaterialFractionalThreshold

@Composable
fun <T : Any> rememberSwipeableState(
    initialValue: T,
    confirmStateChange: (newValue: T) -> Boolean = { true }
): SwipeableState<T> {
    return materialRememberSwipeableState(
        initialValue = initialValue,
        confirmStateChange = confirmStateChange
    )
}

fun <T> Modifier.swipeable(
    state: SwipeableState<T>,
    anchors: Map<Float, T>,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    thresholds: (from: T, to: T) -> ThresholdConfig = { _, _ -> FixedThreshold(56.dp) },
): Modifier {
    return materialSwipeable(
        state = state,
        anchors = anchors,
        orientation = orientation,
        enabled = enabled,
        reverseDirection = reverseDirection,
        interactionSource = interactionSource,
        thresholds = thresholds
    )
}
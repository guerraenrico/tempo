package com.enricog.core.compose.api.modifiers.swipeable

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.SwipeableDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.autoSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.material.FixedThreshold as MaterialFixedThreshold
import androidx.compose.material.FractionalThreshold as MaterialFractionalThreshold
import androidx.compose.material.SwipeableState as MaterialSwipeableState
import androidx.compose.material.ThresholdConfig as MaterialThresholdConfig
import androidx.compose.material.swipeable as materialSwipeable

open class SwipeableState<T>(
    initialValue: T,
    private val animationSpec: AnimationSpec<Float> = SpringSpec(),
    confirmStateChange: (newValue: T) -> Boolean = { true }
) {
    internal val materialSwipeableState = MaterialSwipeableState(
        initialValue = initialValue,
        animationSpec = animationSpec,
        confirmStateChange = confirmStateChange
    )

    val currentValue: T
        get() = materialSwipeableState.currentValue

    val offset: State<Float> get() = materialSwipeableState.offset

    suspend fun fling(velocity: Float) {
        materialSwipeableState.performFling(velocity = velocity)
    }

    fun drag(delta: Float): Float {
        return materialSwipeableState.performDrag(delta = delta)
    }

    suspend fun animateTo(targetValue: T, anim: AnimationSpec<Float> = animationSpec) {
        materialSwipeableState.animateTo(targetValue = targetValue, anim = anim)
    }

    companion object {

        fun <T : Any> Saver(
            animationSpec: AnimationSpec<Float>,
            confirmStateChange: (T) -> Boolean,
            valueSaver: Saver<T, Any>,
        ) = Saver<SwipeableState<T>, Any>(
            save = { swipeableState ->
                with(valueSaver) { save(swipeableState.currentValue) }
            },
            restore = { saveableValue ->
                valueSaver.restore(saveableValue)?.let { value ->
                    SwipeableState(
                        initialValue = value,
                        animationSpec = animationSpec,
                        confirmStateChange = confirmStateChange
                    )
                }
            }
        )
    }
}

@Stable
interface ThresholdConfig

@Immutable
data class FixedThreshold(internal val offset: Dp) : ThresholdConfig

@Immutable
data class FractionalThreshold(
    /*@FloatRange(from = 0.0, to = 1.0)*/
    internal val fraction: Float
) : ThresholdConfig

internal fun ThresholdConfig.toMaterialThresholdConfig(): MaterialThresholdConfig {
    return when (this) {
        is FixedThreshold -> MaterialFixedThreshold(offset = offset)
        is FractionalThreshold -> MaterialFractionalThreshold(fraction = fraction)
        else -> throw NotImplementedError("$this is not a supported ${ThresholdConfig::class.simpleName}")
    }
}


@Composable
@Suppress("UNCHECKED_CAST")
fun <T : Any> rememberSwipeableState(
    initialValue: T,
    valueSaver: Saver<T, out Any> = autoSaver(),
    animationSpec: AnimationSpec<Float> = SpringSpec(),
    confirmStateChange: (newValue: T) -> Boolean = { true }
): SwipeableState<T> {
    return rememberSaveable(
        saver = SwipeableState.Saver(
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange,
            valueSaver = valueSaver as Saver<T, Any>
        )
    ) {
        SwipeableState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            confirmStateChange = confirmStateChange
        )
    }
}

fun <T> Modifier.swipeable(
    state: SwipeableState<T>,
    anchors: Map<Float, T>,
    orientation: Orientation,
    enabled: Boolean = true,
    reverseDirection: Boolean = false,
    interactionSource: MutableInteractionSource? = null,
    thresholds: (from: T, to: T) -> ThresholdConfig = { _, _ -> FixedThreshold(56.dp) },
    velocityThreshold: Dp = SwipeableDefaults.VelocityThreshold
): Modifier {
    return materialSwipeable(
        state = state.materialSwipeableState,
        anchors = anchors,
        orientation = orientation,
        enabled = enabled,
        reverseDirection = reverseDirection,
        interactionSource = interactionSource,
        thresholds = { from, to -> thresholds(from, to).toMaterialThresholdConfig() },
        velocityThreshold = velocityThreshold,
    )
}
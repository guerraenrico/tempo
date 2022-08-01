package com.enricog.ui.components.bottomSheet.layout

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.SpringSpec
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enricog.core.compose.api.modifiers.swipeable.SwipeableState
import com.enricog.ui.components.bottomSheet.navigator.BottomSheetNavigator
import com.enricog.ui.theme.TempoTheme
import com.enricog.ui.theme.contentColorFor
import androidx.compose.material.ModalBottomSheetLayout as MaterialModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState as MaterialModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue as MaterialModalBottomSheetValue

@Composable
fun ModalBottomSheetLayout(
    sheetContent: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    sheetState: ModalBottomSheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden),
    sheetShape: Shape = TempoTheme.shapes.large,
    sheetElevation: Dp = 16.dp,
    sheetBackgroundColor: Color = TempoTheme.colors.surface,
    sheetContentColor: Color = TempoTheme.colors.contentColorFor(sheetBackgroundColor),
    scrimColor: Color = TempoTheme.colors.onSurface.copy(alpha = 0.32f),
    content: @Composable () -> Unit
) {
    MaterialModalBottomSheetLayout(
        sheetContent = sheetContent,
        modifier = modifier,
        sheetState = sheetState.materialState,
        sheetShape = sheetShape,
        sheetElevation = sheetElevation,
        sheetContentColor = sheetContentColor,
        scrimColor = scrimColor,
        content = content
    )
}

@Composable
fun ModalBottomSheetLayout(
    bottomSheetNavigator: BottomSheetNavigator,
    modifier: Modifier = Modifier,
    sheetShape: Shape = TempoTheme.shapes.large,
    sheetElevation: Dp = 16.dp,
    sheetBackgroundColor: Color = TempoTheme.colors.surface,
    sheetContentColor: Color = TempoTheme.colors.contentColorFor(sheetBackgroundColor),
    scrimColor: Color = TempoTheme.colors.onSurface.copy(alpha = 0.32f),
    content: @Composable () -> Unit
) {
    MaterialModalBottomSheetLayout(
        sheetContent = bottomSheetNavigator.sheetContent,
        sheetState = bottomSheetNavigator.sheetState.materialState,
        modifier = modifier,
        sheetShape = sheetShape,
        sheetElevation = sheetElevation,
        sheetContentColor = sheetContentColor,
        scrimColor = scrimColor,
        content = content
    )
}

class ModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    animationSpec: AnimationSpec<Float> = SpringSpec(),
    isSkipHalfExpanded: Boolean,
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = { true }
) : SwipeableState<ModalBottomSheetValue>(
    initialValue = initialValue,
    animationSpec = animationSpec,
    confirmStateChange = confirmStateChange
) {

    internal val materialState = MaterialModalBottomSheetState(
        initialValue = initialValue.toMaterial(),
        animationSpec = animationSpec,
        isSkipHalfExpanded = isSkipHalfExpanded,
        confirmStateChange = { materialModalBottomSheetValue ->
            confirmStateChange(materialModalBottomSheetValue.toInternal())
        }
    )

    suspend fun show() {
        materialState.show()
    }

    suspend fun hide() {
        materialState.hide()
    }

    companion object {
        fun Saver(
            animationSpec: AnimationSpec<Float>,
            skipHalfExpanded: Boolean,
            confirmStateChange: (ModalBottomSheetValue) -> Boolean
        ): Saver<ModalBottomSheetState, *> = Saver(
            save = { it.currentValue },
            restore = {
                ModalBottomSheetState(
                    initialValue = it,
                    animationSpec = animationSpec,
                    isSkipHalfExpanded = skipHalfExpanded,
                    confirmStateChange = confirmStateChange
                )
            }
        )
    }
}

@Composable
fun rememberModalBottomSheetState(
    initialValue: ModalBottomSheetValue,
    skipHalfExpanded: Boolean = false,
    animationSpec: AnimationSpec<Float> = SpringSpec(),
    confirmStateChange: (ModalBottomSheetValue) -> Boolean = { true }
): ModalBottomSheetState {
    return rememberSaveable(
        initialValue, animationSpec, skipHalfExpanded, confirmStateChange,
        saver = ModalBottomSheetState.Saver(
            animationSpec = animationSpec,
            skipHalfExpanded = skipHalfExpanded,
            confirmStateChange = confirmStateChange
        )
    ) {
        ModalBottomSheetState(
            initialValue = initialValue,
            animationSpec = animationSpec,
            isSkipHalfExpanded = skipHalfExpanded,
            confirmStateChange = confirmStateChange
        )
    }
}

enum class ModalBottomSheetValue {
    Hidden,
    Expanded,
    HalfExpanded
}

private fun ModalBottomSheetValue.toMaterial(): MaterialModalBottomSheetValue {
    return when (this) {
        ModalBottomSheetValue.Hidden -> MaterialModalBottomSheetValue.Hidden
        ModalBottomSheetValue.Expanded -> MaterialModalBottomSheetValue.Expanded
        ModalBottomSheetValue.HalfExpanded -> MaterialModalBottomSheetValue.HalfExpanded
    }
}

private fun MaterialModalBottomSheetValue.toInternal(): ModalBottomSheetValue {
    return when (this) {
        MaterialModalBottomSheetValue.Hidden -> ModalBottomSheetValue.Hidden
        MaterialModalBottomSheetValue.Expanded -> ModalBottomSheetValue.Expanded
        MaterialModalBottomSheetValue.HalfExpanded -> ModalBottomSheetValue.HalfExpanded
    }
}

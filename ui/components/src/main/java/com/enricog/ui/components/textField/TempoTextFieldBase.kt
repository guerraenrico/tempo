package com.enricog.ui.components.textField

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun TempoTextFieldBase(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier,
    label: String?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    errorMessage: String?,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int
) {
    require(errorMessage == null || errorMessage.isNotBlank()) { "Error message cannot be blank" }
    require(label == null || label.isNotBlank()) { "Label cannot be blank" }

    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    val transformedText = remember(value.annotatedString, visualTransformation) {
        visualTransformation.filter(value.annotatedString)
    }.text

    val inputPhase = when {
        isFocused -> InputPhase.Focused
        transformedText.isEmpty() -> InputPhase.UnfocusedEmpty
        else -> InputPhase.UnfocusedNotEmpty
    }

    val inputPhaseTransition = updateTransition(
        targetState = inputPhase,
        label = "TempoTextFieldBaseLabelTransition"
    )
    val labelTextSize by inputPhaseTransition.animateInt(
        label = "LabelTextSize",
        transitionSpec = { TempoTextFieldBaseDefaults.labelTextSizeTransitionSpec }
    ) {
        when (it) {
            InputPhase.Focused -> 12
            InputPhase.UnfocusedEmpty -> 18
            InputPhase.UnfocusedNotEmpty -> 12
        }
    }

    val composableLabel: @Composable (() -> Unit)? = label?.let {
        @Composable { TempoTextFieldBaseLabelText(label = it, fontSize = labelTextSize.sp) }
    }
    val isError = errorMessage != null
    val shape = TempoTextFieldBaseDefaults.shape

    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .clip(shape)
                .fillMaxSize()
                .semantics { if (isError) error(requireNotNull(errorMessage)) },
            textStyle = TempoTextFieldBaseDefaults.textStyle,
            label = composableLabel,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = shape,
            colors = TempoTextFieldBaseColors,
            interactionSource = interactionSource
        )
        if (errorMessage != null) {
            TempoTextFieldBaseErrorText(errorMessage)
        }
    }
}

@Composable
private fun TempoTextFieldBaseErrorText(message: String) {
    TempoText(
        modifier = Modifier.padding(top = TempoTheme.dimensions.spaceS),
        text = message,
        style = TempoTheme.typography.caption.copy(
            color = TempoTheme.colors.error
        ),
        maxLines = 1,
    )
}

@Composable
private fun TempoTextFieldBaseLabelText(label: String, fontSize: TextUnit) {
    TempoText(
        text = label,
        style = TempoTheme.typography.caption.copy(fontSize = fontSize),
        maxLines = 1,
    )
}

private object TempoTextFieldBaseDefaults {

    val shape: Shape
        @Composable
        get() = TempoTheme.shapes.textField

    val textStyle: TextStyle
        @Composable
        get() = TempoTheme.typography.textField

    val labelTextSizeTransitionSpec: FiniteAnimationSpec<Int> = spring(visibilityThreshold = 1)
}

private object TempoTextFieldBaseColors : TextFieldColors {

    private const val BackgroundOpacity = 0.06f
    private const val UnfocusedIndicatorLineOpacity = 0.30f

    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        val backgroundColor = TempoTheme.colors.onSurface
            .copy(alpha = BackgroundOpacity)
        return rememberUpdatedState(backgroundColor)
    }

    @Composable
    override fun cursorColor(isError: Boolean): State<Color> {
        val cursorDefaultColor = TempoTheme.colors.primary
        val cursorErrorColor = TempoTheme.colors.error
        return rememberUpdatedState(if (isError) cursorErrorColor else cursorDefaultColor)
    }

    @Composable
    override fun indicatorColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val indicatorFocusedColor = TempoTheme.colors.primary
            .copy(alpha = ContentAlpha.high)
        val indicatorUnfocusedColor = TempoTheme.colors.onSurface
            .copy(alpha = UnfocusedIndicatorLineOpacity)
        val indicatorDisabledColor = indicatorUnfocusedColor
            .copy(alpha = ContentAlpha.disabled)
        val indicatorErrorColor = TempoTheme.colors.error

        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> indicatorDisabledColor
            isError -> indicatorErrorColor
            focused -> indicatorFocusedColor
            else -> indicatorUnfocusedColor
        }
        return if (enabled) {
            animateColorAsState(
                targetValue = targetValue,
                animationSpec = tween(durationMillis = 150)
            )
        } else {
            rememberUpdatedState(targetValue)
        }
    }

    @Composable
    override fun labelColor(
        enabled: Boolean,
        error: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val labelFocusedColor = TempoTheme.colors.primary
            .copy(alpha = ContentAlpha.high)
        val labelUnfocusedColor = TempoTheme.colors.onSurface
            .copy(alpha = ContentAlpha.medium)
        val labelDisabledColor = labelUnfocusedColor
            .copy(alpha = ContentAlpha.disabled)
        val labelErrorColor = TempoTheme.colors.error

        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> labelDisabledColor
            error -> labelErrorColor
            focused -> labelFocusedColor
            else -> labelUnfocusedColor
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    override fun placeholderColor(enabled: Boolean): State<Color> {
        val placeholderColor = TempoTheme.colors.onSurface
            .copy(alpha = ContentAlpha.medium)
        val placeholderDisabledColor = placeholderColor
            .copy(alpha = ContentAlpha.disabled)

        return rememberUpdatedState(if (enabled) placeholderColor else placeholderDisabledColor)
    }

    @Composable
    override fun textColor(enabled: Boolean): State<Color> {
        val textColor = TempoTheme.colors.onSurface.copy(alpha = 1f)
        val textDisabledColor = textColor.copy(alpha = ContentAlpha.disabled)

        return rememberUpdatedState(if (enabled) textColor else textDisabledColor)
    }

    @Composable
    override fun leadingIconColor(enabled: Boolean, isError: Boolean): State<Color> {
        val leadingIconColor = TempoTheme.colors.onSurface
            .copy(alpha = 0.54f)
        val leadingIconDisabledColor = leadingIconColor
            .copy(alpha = ContentAlpha.disabled)

        return rememberUpdatedState(
            when {
                !enabled -> leadingIconDisabledColor
                isError -> leadingIconColor
                else -> leadingIconColor
            }
        )
    }

    @Composable
    override fun trailingIconColor(enabled: Boolean, isError: Boolean): State<Color> {
        val trailingIconColor = TempoTheme.colors.onSurface
            .copy(alpha = 0.54f)
        val trailingIconDisabledColor = trailingIconColor
            .copy(alpha = ContentAlpha.disabled)
        val trailingIconErrorColor = TempoTheme.colors.error

        return rememberUpdatedState(
            when {
                !enabled -> trailingIconDisabledColor
                isError -> trailingIconErrorColor
                else -> trailingIconColor
            }
        )
    }
}

private enum class InputPhase {
    // Text field is focused
    Focused,

    // Text field is not focused and input text is empty
    UnfocusedEmpty,

    // Text field is not focused but input text is not empty
    UnfocusedNotEmpty
}
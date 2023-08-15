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
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
internal fun TempoTextFieldBase(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    labelText: String?,
    supportingText: String?,
    errorText: String?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TempoTextFieldBaseDefaults.textStyle,
    showBackground: Boolean = true,
    showIndicator: Boolean = true,
    readOnly: Boolean = false
) {
    require(errorText == null || errorText.isNotBlank()) { "Error text cannot be blank" }
    require(labelText == null || labelText.isNotBlank()) { "Label text cannot be blank" }
    require(supportingText == null || supportingText.isNotBlank()) { "Supporting text cannot be blank" }

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

    val label: @Composable (() -> Unit)? = labelText?.let {
        @Composable { TempoTextFieldBaseLabelText(label = it, fontSize = labelTextSize.sp) }
    }
    val isError = errorText != null
    val shape = TempoTextFieldBaseDefaults.shape

    Column(modifier = modifier) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .clip(shape)
                .fillMaxWidth()
                .semantics { if (isError) error(requireNotNull(errorText)) },
            textStyle = TempoTextFieldBaseDefaults.textStyle.merge(textStyle),
            label = label,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = isError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
            shape = shape,
            colors = TempoTextFieldBaseDefaults.fieldColors(
                showBackground = showBackground,
                showIndicator = showIndicator
            ),
            interactionSource = interactionSource,
            readOnly = readOnly
        )
        Box(
            modifier = Modifier
                .height(32.dp)
                .padding(
                    horizontal = TempoTheme.dimensions.spaceS,
                    vertical = TempoTheme.dimensions.spaceXS
                )
        ) {
            when {
                errorText != null -> TempoTextFieldBaseErrorText(errorText)
                supportingText != null -> TempoTextFieldBaseSupportingText(supportingText)
            }
        }
    }
}

@Composable
private fun TempoTextFieldBaseErrorText(message: String) {
    TempoText(
        text = message,
        style = TempoTheme.typography.caption.copy(
            color = TempoTheme.colors.error
        ),
        maxLines = 1,
    )
}

@Composable
private fun TempoTextFieldBaseSupportingText(message: String) {
    TempoText(
        text = message,
        style = TempoTheme.typography.caption,
        maxLines = 1,
    )
}

/**
 * Uses [LocalContentColor] since in the material library
 * the label text color are changed using a [CompositionLocalProvider]
 * The color is the same one defined in [TempoTextFieldBaseColors.labelColor]
 */
@Composable
private fun TempoTextFieldBaseLabelText(label: String, fontSize: TextUnit) {
    TempoText(
        text = label,
        style = TempoTheme.typography.caption.copy(
            fontSize = fontSize,
            color = LocalContentColor.current
        ),
        maxLines = 1,
    )
}

private object TempoTextFieldBaseDefaults {

    private const val BackgroundOpacity = 0.06f
    private const val UnfocusedIndicatorLineOpacity = 0.30f

    val shape: Shape
        @Composable
        get() = TempoTheme.shapes.textField

    val textStyle: TextStyle
        @Composable
        get() = TempoTheme.typography.textField

    val labelTextSizeTransitionSpec: FiniteAnimationSpec<Int> = spring(visibilityThreshold = 1)

    @Composable
    fun fieldColors(showBackground: Boolean, showIndicator: Boolean): TextFieldColors {
        return TempoTextFieldBaseColors(
            // Background
            backgroundColor = TempoTheme.colors.onSurface
                .copy(alpha = if (showBackground) BackgroundOpacity else 0f),
            // Cursor
            cursorDefaultColor = TempoTheme.colors.primary,
            cursorErrorColor = TempoTheme.colors.error,
            // Indicator
            indicatorFocusedColor = TempoTheme.colors.primary
                .copy(alpha = if (showIndicator) ContentAlpha.high else 0f),
            indicatorUnfocusedColor = TempoTheme.colors.onSurface
                .copy(alpha = if (showIndicator) UnfocusedIndicatorLineOpacity else 0f),
            indicatorDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = if (showIndicator) ContentAlpha.disabled else 0f),
            indicatorErrorColor = TempoTheme.colors.error
                .copy(alpha = if (showIndicator) 1f else 0f),
            // Label
            labelFocusedColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.high),
            labelUnfocusedColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.medium),
            labelDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled),
            labelErrorColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.high),
            // Placeholder
            placeholderColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.medium),
            placeholderDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled),
            // Text
            textColor = TempoTheme.colors.onSurface
                .copy(alpha = 1f),
            textDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled),
            // Leading Icon
            leadingIconColor = TempoTheme.colors.onSurface
                .copy(alpha = 0.54f),
            leadingIconDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled),
            // Trailing Icon
            trailingIconColor = TempoTheme.colors.onSurface
                .copy(alpha = 0.54f),
            trailingIconDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled),
            trailingIconErrorColor = TempoTheme.colors.error,
        )
    }
}

@Immutable
private class TempoTextFieldBaseColors(
    // Background
    private val backgroundColor: Color,
    // Cursor
    private val cursorDefaultColor: Color,
    private val cursorErrorColor: Color,
    // Indicator
    private val indicatorFocusedColor: Color,
    private val indicatorUnfocusedColor: Color,
    private val indicatorDisabledColor: Color,
    private val indicatorErrorColor: Color,
    // Label
    private val labelFocusedColor: Color,
    private val labelUnfocusedColor: Color,
    private val labelDisabledColor: Color,
    private val labelErrorColor: Color,
    // Placeholder
    private val placeholderColor: Color,
    private val placeholderDisabledColor: Color,
    // Text
    private val textColor: Color,
    private val textDisabledColor: Color,
    // Leading Icon
    private val leadingIconColor: Color,
    private val leadingIconDisabledColor: Color,
    // Trailing Icon
    private val trailingIconColor: Color,
    private val trailingIconDisabledColor: Color,
    private val trailingIconErrorColor: Color,
) : TextFieldColors {

    @Composable
    override fun backgroundColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(backgroundColor)
    }

    @Composable
    override fun cursorColor(isError: Boolean): State<Color> {
        return rememberUpdatedState(if (isError) cursorErrorColor else cursorDefaultColor)
    }

    @Composable
    override fun indicatorColor(
        enabled: Boolean,
        isError: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
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
                animationSpec = tween(durationMillis = 150),
                label = "TempoTextFieldIndicatorColor"
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
        return rememberUpdatedState(if (enabled) placeholderColor else placeholderDisabledColor)
    }

    @Composable
    override fun textColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) textColor else textDisabledColor)
    }

    @Composable
    override fun leadingIconColor(enabled: Boolean, isError: Boolean): State<Color> {
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
        return rememberUpdatedState(
            when {
                !enabled -> trailingIconDisabledColor
                isError -> trailingIconErrorColor
                else -> trailingIconColor
            }
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TempoTextFieldBaseColors

        if (backgroundColor != other.backgroundColor) return false
        if (cursorDefaultColor != other.cursorDefaultColor) return false
        if (cursorErrorColor != other.cursorErrorColor) return false
        if (indicatorFocusedColor != other.indicatorFocusedColor) return false
        if (indicatorUnfocusedColor != other.indicatorUnfocusedColor) return false
        if (indicatorDisabledColor != other.indicatorDisabledColor) return false
        if (indicatorErrorColor != other.indicatorErrorColor) return false
        if (labelFocusedColor != other.labelFocusedColor) return false
        if (labelUnfocusedColor != other.labelUnfocusedColor) return false
        if (labelDisabledColor != other.labelDisabledColor) return false
        if (labelErrorColor != other.labelErrorColor) return false
        if (placeholderColor != other.placeholderColor) return false
        if (placeholderDisabledColor != other.placeholderDisabledColor) return false
        if (textColor != other.textColor) return false
        if (textDisabledColor != other.textDisabledColor) return false
        if (leadingIconColor != other.leadingIconColor) return false
        if (leadingIconDisabledColor != other.leadingIconDisabledColor) return false
        if (trailingIconColor != other.trailingIconColor) return false
        if (trailingIconDisabledColor != other.trailingIconDisabledColor) return false
        if (trailingIconErrorColor != other.trailingIconErrorColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backgroundColor.hashCode()
        result = 31 * result + cursorDefaultColor.hashCode()
        result = 31 * result + cursorErrorColor.hashCode()
        result = 31 * result + indicatorFocusedColor.hashCode()
        result = 31 * result + indicatorUnfocusedColor.hashCode()
        result = 31 * result + indicatorDisabledColor.hashCode()
        result = 31 * result + indicatorErrorColor.hashCode()
        result = 31 * result + labelFocusedColor.hashCode()
        result = 31 * result + labelUnfocusedColor.hashCode()
        result = 31 * result + labelDisabledColor.hashCode()
        result = 31 * result + labelErrorColor.hashCode()
        result = 31 * result + placeholderColor.hashCode()
        result = 31 * result + placeholderDisabledColor.hashCode()
        result = 31 * result + textColor.hashCode()
        result = 31 * result + textDisabledColor.hashCode()
        result = 31 * result + leadingIconColor.hashCode()
        result = 31 * result + leadingIconDisabledColor.hashCode()
        result = 31 * result + trailingIconColor.hashCode()
        result = 31 * result + trailingIconDisabledColor.hashCode()
        result = 31 * result + trailingIconErrorColor.hashCode()
        return result
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
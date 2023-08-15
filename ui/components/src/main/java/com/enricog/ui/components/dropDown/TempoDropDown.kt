package com.enricog.ui.components.dropDown

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ContentAlpha
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.enricog.core.compose.api.classes.ImmutableList
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
fun TempoDropDown(
    items: ImmutableList<TempoDropDownItem>,
    selectedItem: TempoDropDownItem,
    onItemSelected: (TempoDropDownItem) -> Unit,
    modifier: Modifier = Modifier,
    labelText: String? = null,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }

    val backgroundColor by TempoDropDownDefaults.colors.backgroundColor()
    val labelColor by TempoDropDownDefaults.colors.labelColor(
        enabled = enabled,
        interactionSource = interactionSource
    )
    val textColor by TempoDropDownDefaults.colors.textColor(enabled = enabled)
    val borderStroke by animateBorderStrokeAsState(
        enabled = enabled,
        interactionSource = interactionSource,
        colors = TempoDropDownDefaults.colors,
        focusedBorderThickness = TempoDropDownDefaults.FocusedBorderThickness,
        unfocusedBorderThickness = TempoDropDownDefaults.UnfocusedBorderThickness
    )

    Box(
        modifier = modifier
            .focusRequester(focusRequester = focusRequester)
            .focusable(
                enabled = enabled,
                interactionSource = interactionSource
            )
            .clickable(
                enabled = enabled,
                interactionSource = interactionSource,
                indication = null
            ) {
                focusRequester.requestFocus()
                expanded = true
            }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = TempoDropDownDefaults.shape
                )
                .clip(shape = TempoDropDownDefaults.shape)
        ) {
            Box(
                modifier = Modifier
                    .defaultMinSize(
                        minHeight = if (labelText == null)
                            TempoDropDownDefaults.defaultMinSizeWithoutLabel
                        else TempoDropDownDefaults.defaultMinSizeWithLabel
                    )
                    .fillMaxWidth()
                    .drawIndicatorLine(borderStroke = borderStroke)
            ) {
                if (labelText != null) {
                    TempoText(
                        text = labelText,
                        style = TempoTheme.typography.caption.copy(
                            fontSize = 12.sp,
                            color = labelColor
                        ),
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = TempoTheme.dimensions.spaceM,
                                vertical = TempoTheme.dimensions.spaceXS
                            )
                    )
                }

                TempoText(
                    text = selectedItem.text.resolveString(),
                    style = TempoDropDownDefaults.textStyle.copy(
                        color = textColor
                    ),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterStart)
                        .padding(top = if (labelText != null) TempoTheme.dimensions.spaceS else 0.dp)
                        .padding(
                            horizontal = TempoTheme.dimensions.spaceM,
                            vertical = TempoTheme.dimensions.spaceXS
                        )
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    for (item in items) {
                        DropdownMenuItem(
                            onClick = {
                                expanded = false
                                onItemSelected(item)
                            }
                        ) {
                            TempoText(text = item.text.resolveString())
                        }
                    }
                }
            }
        }
    }
}

private object TempoDropDownDefaults {

    const val AnimationDuration = 150

    val defaultMinSizeWithoutLabel = 62.dp

    val defaultMinSizeWithLabel = 64.dp

    val UnfocusedBorderThickness = 1.dp

    val FocusedBorderThickness = 2.dp

    val shape: Shape
        @Composable
        get() = TempoTheme.shapes.textField

    val textStyle: TextStyle
        @Composable
        get() = TempoTheme.typography.textField

    val colors: TempoDropDownColors
        @Composable
        get() = TempoDropDownColors(
            // Background
            backgroundColor = TempoTheme.colors.onSurface
                .copy(alpha = 0.07f),
            // Indicator
            indicatorFocusedColor = TempoTheme.colors.primary,
            indicatorUnfocusedColor = TempoTheme.colors.onSurface
                .copy(alpha = .3f),
            indicatorDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = .1f),
            // Label
            labelFocusedColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.high),
            labelUnfocusedColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.medium),
            labelDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled),
            // Text
            textColor = TempoTheme.colors.onSurface
                .copy(alpha = 1f),
            textDisabledColor = TempoTheme.colors.onSurface
                .copy(alpha = ContentAlpha.disabled)
        )
}

@Immutable
private class TempoDropDownColors(
    // Background
    private val backgroundColor: Color,
    // Indicator
    private val indicatorFocusedColor: Color,
    private val indicatorUnfocusedColor: Color,
    private val indicatorDisabledColor: Color,
    // Label
    private val labelFocusedColor: Color,
    private val labelUnfocusedColor: Color,
    private val labelDisabledColor: Color,
    // Text
    private val textColor: Color,
    private val textDisabledColor: Color,
) {

    @Composable
    fun backgroundColor(): State<Color> {
        return rememberUpdatedState(backgroundColor)
    }

    @Composable
    fun indicatorColor(
        enabled: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> indicatorDisabledColor
            focused -> indicatorFocusedColor
            else -> indicatorUnfocusedColor
        }
        return if (enabled) {
            animateColorAsState(
                targetValue = targetValue,
                animationSpec = tween(durationMillis = 150),
                label = "TempoDropDownIndicatorColor"
            )
        } else {
            rememberUpdatedState(targetValue)
        }
    }

    @Composable
    fun labelColor(
        enabled: Boolean,
        interactionSource: InteractionSource
    ): State<Color> {
        val focused by interactionSource.collectIsFocusedAsState()

        val targetValue = when {
            !enabled -> labelDisabledColor
            focused -> labelFocusedColor
            else -> labelUnfocusedColor
        }
        return rememberUpdatedState(targetValue)
    }

    @Composable
    fun textColor(enabled: Boolean): State<Color> {
        return rememberUpdatedState(if (enabled) textColor else textDisabledColor)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TempoDropDownColors

        if (backgroundColor != other.backgroundColor) return false
        if (indicatorFocusedColor != other.indicatorFocusedColor) return false
        if (indicatorUnfocusedColor != other.indicatorUnfocusedColor) return false
        if (indicatorDisabledColor != other.indicatorDisabledColor) return false
        if (labelFocusedColor != other.labelFocusedColor) return false
        if (labelUnfocusedColor != other.labelUnfocusedColor) return false
        if (labelDisabledColor != other.labelDisabledColor) return false
        if (textColor != other.textColor) return false
        if (textDisabledColor != other.textDisabledColor) return false

        return true
    }

    override fun hashCode(): Int {
        var result = backgroundColor.hashCode()
        result = 31 * result + indicatorFocusedColor.hashCode()
        result = 31 * result + indicatorUnfocusedColor.hashCode()
        result = 31 * result + indicatorDisabledColor.hashCode()
        result = 31 * result + labelFocusedColor.hashCode()
        result = 31 * result + labelUnfocusedColor.hashCode()
        result = 31 * result + labelDisabledColor.hashCode()
        result = 31 * result + textColor.hashCode()
        result = 31 * result + textDisabledColor.hashCode()
        return result
    }
}

private fun Modifier.drawIndicatorLine(borderStroke: BorderStroke): Modifier {
    return drawWithContent {
        drawContent()
        if (borderStroke.width == Dp.Hairline) return@drawWithContent
        val strokeWidth = borderStroke.width.value * density
        val y = size.height - strokeWidth / 2
        drawLine(
            brush = borderStroke.brush,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidth
        )
    }
}

@Composable
private fun animateBorderStrokeAsState(
    enabled: Boolean,
    interactionSource: InteractionSource,
    colors: TempoDropDownColors,
    focusedBorderThickness: Dp,
    unfocusedBorderThickness: Dp
): State<BorderStroke> {
    val focused by interactionSource.collectIsFocusedAsState()
    val indicatorColor by colors.indicatorColor(
        enabled = enabled,
        interactionSource = interactionSource
    )
    val targetThickness = if (focused) focusedBorderThickness else unfocusedBorderThickness
    val animatedThickness = if (enabled) {
        animateDpAsState(
            targetValue = targetThickness,
            animationSpec = tween(durationMillis = TempoDropDownDefaults.AnimationDuration),
            label = "TempoDropDownBorderStrokeAnimation"
        )
    } else {
        rememberUpdatedState(unfocusedBorderThickness)
    }
    return rememberUpdatedState(
        BorderStroke(
            width = animatedThickness.value,
            brush = SolidColor(value = indicatorColor)
        )
    )
}

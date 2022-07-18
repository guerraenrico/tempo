package com.enricog.ui.components.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.enricog.ui.components.R
import com.enricog.ui.components.icon.TempoIcon
import com.enricog.ui.components.icon.TempoIconSize
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
fun TempoButton(
    onClick: () -> Unit,
    text: String,
    contentDescription: String,
    modifier: Modifier = Modifier,
    icon: Painter? = null,
    iconSpacing: Dp = TempoButtonDefaults.IconSpacing,
    color: TempoButtonColor = TempoButtonColor.Normal,
    enabled: Boolean = true
) {
    require(contentDescription.isNotBlank()) { "contentDescription cannot be blank" }
    require(text.isNotBlank()) { "text cannot be blank, use TempoIconButton if you need to show only an icon" }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = color.buttonColors(),
        shape = TempoButtonDefaults.Shape,
        elevation = TempoButtonDefaults.Elevation,
        contentPadding = TempoButtonDefaults.ContentPadding
    ) {
        if (icon != null) {
            TempoButtonIcon(
                icon = icon,
                color = color,
                contentDescription = contentDescription,
            )
            Spacer(modifier = Modifier.width(iconSpacing))
        }
        TempoButtonText(text = text, color = color)
    }
}

@Composable
private fun TempoButtonIcon(
    icon: Painter,
    contentDescription: String,
    modifier: Modifier = Modifier,
    color: TempoButtonColor = TempoButtonColor.Normal,
    enabled: Boolean = true
) {
    TempoIcon(
        modifier = modifier,
        icon = icon,
        color = color.buttonColors().contentColor(enabled).value,
        contentDescription = contentDescription,
        size = TempoIconSize.Normal
    )
}

@Composable
private fun TempoButtonText(
    text: String,
    modifier: Modifier = Modifier,
    color: TempoButtonColor = TempoButtonColor.Normal,
    enabled: Boolean = true
) {
    TempoText(
        modifier = modifier,
        text = text,
        style = TempoTheme.typography.button.copy(
            color = color.buttonColors().contentColor(enabled).value
        )
    )
}

private object TempoButtonDefaults {

    val Shape: Shape
        @Composable
        @ReadOnlyComposable
        get() = TempoTheme.shapes.button

    val ContentPadding: PaddingValues
        @Composable
        @ReadOnlyComposable
        get() = PaddingValues(
            start = 14.dp,
            top = 14.dp,
            end = 14.dp,
            bottom = 14.dp,
        )

    val Elevation: ButtonElevation
        @Composable
        get() = ButtonDefaults.elevation(
            defaultElevation = 0.dp,
            pressedElevation = 0.dp,
            disabledElevation = 0.dp,
            hoveredElevation = 0.dp,
            focusedElevation = 0.dp
        )

    val IconSpacing: Dp
        @Composable
        @ReadOnlyComposable
        get() = TempoTheme.dimensions.spaceM
}

@Preview
@Composable
private fun Preview() {
    TempoButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = {},
        text = "Button",
        contentDescription = "content description",
        icon = painterResource(id = R.drawable.ic_back)
    )
}

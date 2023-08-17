package com.enricog.ui.components.toolbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.enricog.ui.components.R
import com.enricog.ui.components.button.icon.TempoIconButton
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
fun TempoToolbar(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    title: String? = null,
    menuContent: (@Composable RowScope.() -> Unit)? = null
) {
    CompositionLocalProvider(
        LocalElevationOverlay provides null
    ) {
        TopAppBar(
            modifier = modifier,
            elevation = 0.dp,
            backgroundColor = TempoTheme.colors.background
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxSize(),
            ) {
                val (backButton, titleText, menu) = createRefs()

                if (onBack != null) {
                    TempoIconButton(
                        modifier = Modifier
                            .constrainAs(backButton) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                bottom.linkTo(parent.bottom)
                            },
                        onClick = onBack,
                        iconResId = R.drawable.ic_back,
                        contentDescription = stringResource(R.string.content_description_toolbar_button_back),
                        drawShadow = false
                    )
                }
                if (title != null) {
                    TempoToolbarText(
                        modifier = Modifier
                            .constrainAs(titleText) {
                                top.linkTo(parent.top)
                                bottom.linkTo(parent.bottom)
                                when {
                                    onBack != null -> start.linkTo(backButton.end)
                                    else -> start.linkTo(parent.start)
                                }
                                when {
                                    menuContent != null -> end.linkTo(menu.start)
                                    else -> end.linkTo(parent.end)
                                }
                                width = Dimension.fillToConstraints
                            }
                            .padding(
                                start = TempoTheme.dimensions.spaceM,
                                end = TempoTheme.dimensions.spaceM
                            ),
                        title = title
                    )
                }
                if (menuContent != null) {
                    Row(
                        modifier = Modifier
                            .constrainAs(menu) {
                                top.linkTo(parent.top)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                            }
                    ) {
                        menuContent()
                    }
                }
            }
        }
    }
}

@Composable
private fun TempoToolbarText(title: String, modifier: Modifier = Modifier) {
    TempoText(
        text = title,
        style = TempoTheme.typography.h1,
        maxLines = 1,
        modifier = modifier
    )
}

package com.enricog.ui_components.common.toolbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.enricog.ui_components.R
import com.enricog.ui_components.resources.dimensions

@Composable
fun TempoToolbar(
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null,
    title: String? = null
) {
    CompositionLocalProvider(
        LocalElevationOverlay provides null
    ) {
        TopAppBar(
            modifier = modifier,
            elevation = 0.dp,
            backgroundColor = MaterialTheme.colors.background
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onBack != null) {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.content_description_toolbar_button_back)
                        )
                    }
                }
                if (title != null) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.h1,
                        maxLines = 1,
                        modifier = Modifier.padding(start = MaterialTheme.dimensions.spaceM),
                    )
                }
            }
        }
    }
}

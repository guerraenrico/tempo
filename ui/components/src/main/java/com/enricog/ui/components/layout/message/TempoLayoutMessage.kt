package com.enricog.ui.components.layout.message

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.enricog.ui.components.R
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.icon.TempoIcon
import com.enricog.ui.components.icon.TempoIconSize
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme

@Composable
fun TempoLayoutMessage(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: (@Composable () -> Unit)? = null,
    button: (@Composable () -> Unit)? = null
) {
    require(title.isNotBlank()) { "title cannot be blank" }
    require(description.isNotBlank()) { "description cannot be blank" }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(all = TempoTheme.dimensions.spaceM),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon?.invoke()
        Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceM))

        TempoText(
            text = title,
            style = TempoTheme.typography.h2,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceS))

        TempoText(
            text = description,
            style = TempoTheme.typography.body1,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceL))

        button?.invoke()
    }
}

@Preview
@Composable
private fun TempoLayoutMessagePreview() {
    TempoLayoutMessage(
        icon = {
            TempoIcon(
                iconResId = R.drawable.ic_error_unknown,
                contentDescription = stringResource(id = R.string.content_description_generic_error_icon),
                size = TempoIconSize.Original,
                color = Color.Unspecified
            )
        },
        title = stringResource(id = R.string.label_generic_error_title),
        description = stringResource(id = R.string.label_generic_error_description),
        button = {
            TempoButton(
                onClick = { },
                text = stringResource(id = R.string.button_retry),
                iconContentDescription = stringResource(id = R.string.content_description_button_retry)
            )
        }
    )
}



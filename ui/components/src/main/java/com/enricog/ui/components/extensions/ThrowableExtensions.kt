package com.enricog.ui.components.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.enricog.ui.components.R
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.icon.TempoIcon
import com.enricog.ui.components.icon.TempoIconSize
import com.enricog.ui.components.layout.error.TempoLayoutError

@Composable
fun Throwable.getLayout(onButtonClick: () -> Unit) {
    when (this) {
        else -> GenericError(onButtonClick = onButtonClick)
    }
}

@Composable
private fun GenericError(onButtonClick: () -> Unit) {
    TempoLayoutError(
        icon = {
            TempoIcon(
                icon = painterResource(id = R.drawable.ic_error_unknown),
                contentDescription = stringResource(id = R.string.content_description_generic_error_icon),
                size = TempoIconSize.Original,
                color = Color.Unspecified
            )
        },
        title = stringResource(id = R.string.label_generic_error_title),
        description = stringResource(id = R.string.label_generic_error_description),
        button = {
            TempoButton(
                onClick = onButtonClick,
                text = stringResource(id = R.string.button_retry),
                contentDescription = stringResource(id = R.string.content_description_button_retry)
            )
        }
    )
}
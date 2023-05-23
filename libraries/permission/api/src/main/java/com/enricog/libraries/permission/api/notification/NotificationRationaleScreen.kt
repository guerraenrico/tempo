package com.enricog.libraries.permission.api.notification

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.enricog.libraries.permission.api.R
import com.enricog.ui.components.button.TempoButton
import com.enricog.ui.components.button.TempoButtonColor
import com.enricog.ui.components.text.TempoText
import com.enricog.ui.theme.TempoTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState

@Composable
internal fun NotificationRationaleScreen(viewModel: NotificationRationaleViewModel) {
    Column(
        modifier = Modifier
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(TempoTheme.dimensions.spaceM)
    ) {
        TempoText(
            style = TempoTheme.typography.h1,
            text = stringResource(id = R.string.permission_notification_label_title)
        )
        Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceS))
        TempoText(
            style = TempoTheme.typography.body1,
            text = stringResource(id = R.string.permission_notification_label_description)
        )
        Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceM))
        Buttons(onContinue = viewModel::onContinue, onCancel = viewModel::onCancel)
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class)
private fun Buttons(onContinue: () -> Unit, onCancel: () -> Unit, modifier: Modifier = Modifier) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
        Column(modifier = modifier) {
            TempoButton(
                modifier = Modifier.fillMaxWidth(),
                color = TempoButtonColor.Normal,
                onClick = onCancel,
                text = stringResource(id = R.string.permission_notification_button_cancel),
            )
            Spacer(modifier = Modifier.height(TempoTheme.dimensions.spaceM))
            TempoButton(
                modifier = Modifier.fillMaxWidth(),
                color = TempoButtonColor.Accent,
                onClick = {
                    onContinue()
                    permissionState.launchPermissionRequest()
                },
                text = stringResource(id = R.string.permission_notification_button_confirm),
            )
        }
    } else {
        TempoButton(
            modifier = modifier.fillMaxWidth(),
            color = TempoButtonColor.Accent,
            onClick = onContinue,
            text = stringResource(id = R.string.permission_notification_button_ok),
        )
    }
}

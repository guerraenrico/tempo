package com.enricog.ui.components.selector

import androidx.compose.material.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import com.enricog.core.compose.api.modifiers.semantics.isChecked

@Composable
fun TempoSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Switch(
        modifier = modifier
            .semantics { isChecked(checked = checked) },
        checked = checked,
        onCheckedChange = onCheckedChange,
        enabled = enabled,
        colors = TempoSwitchColor.colors()
    )
}

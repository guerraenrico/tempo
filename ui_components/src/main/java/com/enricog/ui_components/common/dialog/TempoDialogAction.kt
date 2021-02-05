package com.enricog.ui_components.common.dialog

data class TempoDialogAction(
    val text: String,
    val contentDescription: String,
    val onClick: () -> Unit
)

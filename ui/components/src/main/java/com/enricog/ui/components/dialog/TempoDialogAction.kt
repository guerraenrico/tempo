package com.enricog.ui.components.dialog

data class TempoDialogAction(
    val text: String,
    val contentDescription: String,
    val onClick: () -> Unit
)

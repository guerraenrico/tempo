package com.enricog.data.timer.api.theme.entities

import com.enricog.core.entities.ID

data class TimerTheme(
    val id: ID,
    val name: String,
    val description: String,
    val preparationTimeResource: Resource,
    val stopwatchResource: Resource,
    val timerResource: Resource,
    val restResource: Resource,
    val isLocked: Boolean
) {

    data class Resource(
        val background: Asset,
        val onBackground: Asset.Color
    )

    sealed class Asset {
        data class Color(val argb: ULong) : Asset()
    }

    companion object
}

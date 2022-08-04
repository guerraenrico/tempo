package com.enricog.ui.theme

import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Shapes
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.dp

@Immutable
class TempoShapes internal constructor() {

    val small: CornerBasedShape = RoundedCornerShape(5.dp)

    val medium: CornerBasedShape = RoundedCornerShape(7.dp)

    val large: CornerBasedShape = RoundedCornerShape(10.dp)

    val listItem: RoundedCornerShape = RoundedCornerShape(10.dp)

    val button: RoundedCornerShape = RoundedCornerShape(10.dp)

    val textField: RoundedCornerShape = RoundedCornerShape(10.dp)

    val bottomSheet: RoundedCornerShape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TempoShapes

        if (small != other.small) return false
        if (medium != other.medium) return false
        if (large != other.large) return false
        if (listItem != other.listItem) return false
        if (button != other.button) return false

        return true
    }

    override fun hashCode(): Int {
        var result = small.hashCode()
        result = 31 * result + medium.hashCode()
        result = 31 * result + large.hashCode()
        result = 31 * result + listItem.hashCode()
        result = 31 * result + button.hashCode()
        return result
    }

    override fun toString(): String {
        return "TempoShapes(small=$small, medium=$medium, large=$large, listItem=$listItem, button=$button)"
    }
}

internal val LocalTempoShapes = compositionLocalOf { TempoShapes() }

internal fun TempoShapes.toMaterialShapes(): Shapes {
    return Shapes(
        small = small,
        medium = medium,
        large = large
    )
}

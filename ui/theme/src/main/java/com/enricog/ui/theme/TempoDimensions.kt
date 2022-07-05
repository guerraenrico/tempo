package com.enricog.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
class TempoDimensions internal constructor() {

    val spaceXS: Dp = 6.dp
    val spaceS: Dp = 10.dp
    val spaceM: Dp = 16.dp
    val spaceL: Dp = 20.dp
    val spaceXL: Dp = 30.dp
    val spaceXXL: Dp = 40.dp

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TempoDimensions

        if (spaceXS != other.spaceXS) return false
        if (spaceS != other.spaceS) return false
        if (spaceM != other.spaceM) return false
        if (spaceL != other.spaceL) return false
        if (spaceXL != other.spaceXL) return false
        if (spaceXXL != other.spaceXXL) return false

        return true
    }

    override fun hashCode(): Int {
        var result = spaceXS.hashCode()
        result = 31 * result + spaceS.hashCode()
        result = 31 * result + spaceM.hashCode()
        result = 31 * result + spaceL.hashCode()
        result = 31 * result + spaceXL.hashCode()
        result = 31 * result + spaceXXL.hashCode()
        return result
    }

    override fun toString(): String {
        return "TempoDimensions(spaceXS=$spaceXS, spaceS=$spaceS, spaceM=$spaceM, spaceL=$spaceL, spaceXL=$spaceXL, spaceXXL=$spaceXXL)"
    }
}

internal val LocalTempoDimensions = compositionLocalOf { TempoDimensions() }

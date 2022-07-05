package com.enricog.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
class TempoTypography internal constructor() {

    val fontFamily: FontFamily = FontFamily(
        Font(resId = R.font.prompt_light, weight = FontWeight.Light),
        Font(resId = R.font.prompt_normal, weight = FontWeight.Normal),
        Font(resId = R.font.prompt_bold, weight = FontWeight.Bold)
    )
    val fontFamilyMono: FontFamily = FontFamily(
        Font(R.font.jetbrains_mono_extra_bold, weight = FontWeight.ExtraBold)
    )

    val h1: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 22.sp
    )
    val h2: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 18.sp
    )
    val h3: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 16.sp
    )
    val h4: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 14.sp
    )
    val h5: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 12.sp
    )
    val h6: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 10.sp
    )
    val body1: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 18.sp
    )
    val body2: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 16.sp
    )
    val button: TextStyle = TextStyle(
        fontWeight = FontWeight.Bold,
        color = white,
        fontSize = 18.sp
    )
    val caption: TextStyle = TextStyle(
        fontWeight = FontWeight.Normal,
        color = white,
        fontSize = 12.sp
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TempoTypography

        if (h1 != other.h1) return false
        if (h2 != other.h2) return false
        if (h3 != other.h3) return false
        if (h4 != other.h4) return false
        if (h5 != other.h5) return false
        if (h6 != other.h6) return false
        if (body1 != other.body1) return false
        if (body2 != other.body2) return false
        if (button != other.button) return false
        if (caption != other.caption) return false

        return true
    }

    override fun hashCode(): Int {
        var result = h1.hashCode()
        result = 31 * result + h2.hashCode()
        result = 31 * result + h3.hashCode()
        result = 31 * result + h4.hashCode()
        result = 31 * result + h5.hashCode()
        result = 31 * result + h6.hashCode()
        result = 31 * result + body1.hashCode()
        result = 31 * result + body2.hashCode()
        result = 31 * result + button.hashCode()
        result = 31 * result + caption.hashCode()
        return result
    }

    override fun toString(): String {
        return "TempoTypography(h1=$h1, h2=$h2, h3=$h3, h4=$h4, h5=$h5, h6=$h6, " +
                "body1=$body1, body2=$body2, button=$button, caption=$caption)"
    }


}

internal val LocalTempoTypography = staticCompositionLocalOf { TempoTypography() }

internal fun TempoTypography.toMaterialTypography(): Typography {
    return Typography(
        defaultFontFamily = fontFamily,
        h1 = h1,
        h2 = h2,
        h3 = h3,
        h4 = h4,
        h5 = h5,
        h6 = h6,
        body1 = body1,
        body2 = body2,
        button = button,
        caption = caption
    )
}

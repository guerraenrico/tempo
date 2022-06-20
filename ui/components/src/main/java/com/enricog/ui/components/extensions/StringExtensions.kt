package com.enricog.ui.components.extensions

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun String.toTextFieldValue(selection: TextRange = TextRange(index = length)): TextFieldValue {
    return TextFieldValue(text = this, selection = selection)
}
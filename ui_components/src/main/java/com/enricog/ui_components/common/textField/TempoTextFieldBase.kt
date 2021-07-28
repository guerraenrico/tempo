package com.enricog.ui_components.common.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.resources.FontFamilyDefault
import com.enricog.ui_components.resources.TempoTheme
import com.enricog.ui_components.resources.white

internal val tempoTextFieldBaseStyle: TextStyle = TextStyle(
    fontFamily = FontFamilyDefault,
    fontWeight = FontWeight.Bold,
    color = white,
    fontSize = 20.sp
)

@Composable
internal fun TempoTextFieldBase(
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit,
    modifier: Modifier,
    textStyle: TextStyle,
    label: String?,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    errorMessage: String?,
    visualTransformation: VisualTransformation,
    keyboardOptions: KeyboardOptions,
    keyboardActions: KeyboardActions,
    singleLine: Boolean,
    maxLines: Int,
) {
    val composableLabel: @Composable (() -> Unit)? = label?.let { @Composable { Text(label) } }
    Column(
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxSize(),
            textStyle = tempoTextFieldBaseStyle.merge(textStyle),
            label = composableLabel,
            leadingIcon = leadingIcon,
            trailingIcon = trailingIcon,
            isError = errorMessage != null,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = singleLine,
            maxLines = maxLines,
        )
        if (errorMessage != null) {
            Text(
                modifier = Modifier.padding(top = TempoTheme.dimensions.spaceS),
                text = errorMessage,
                style = TempoTheme.typography.caption,
                maxLines = 1,
                color = TempoTheme.colors.error
            )
        }
    }
}

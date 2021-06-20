package com.enricog.ui_components.common.textField

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.enricog.ui_components.resources.FontFamilyDefault
import com.enricog.ui_components.resources.dimensions
import com.enricog.ui_components.resources.white
import kotlin.time.Duration

private val textFieldStyle: TextStyle = TextStyle(
    fontFamily = FontFamilyDefault,
    fontWeight = FontWeight.Bold,
    color = white,
    fontSize = 20.sp
)

@Composable
fun TempoTimeField(
    value: Duration,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String?,
    errorMessage: String?,
    keyboardOptions: KeyboardOptions
) {
    val composableLabel: @Composable (() -> Unit)? = label?.let { @Composable { Text(label) } }

    Column(
        modifier = modifier
    ) {
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxSize(),
            textStyle = textFieldStyle,
            label = composableLabel,
            isError = errorMessage != null,
            keyboardOptions = keyboardOptions,
            singleLine = true,
            maxLines = 1
        )
        if (errorMessage != null) {
            Text(
                modifier = Modifier.padding(top = MaterialTheme.dimensions.spaceS),
                text = errorMessage,
                style = MaterialTheme.typography.caption,
                maxLines = 1,
                color = MaterialTheme.colors.error
            )
        }
    }
}

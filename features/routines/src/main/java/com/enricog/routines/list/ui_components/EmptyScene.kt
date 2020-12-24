package com.enricog.routines.list.ui_components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enricog.ui_components.common.button.TempoButton

@Composable
internal fun EmptyScene(onCreateSegmentClick: () -> Unit) {
    Column(
      modifier = Modifier
          .fillMaxHeight()
          .fillMaxWidth()
    ) {
        Text(text = "")
        Spacer(modifier = Modifier.height(40.dp))
        TempoButton(onClick = onCreateSegmentClick, text = "")
    }
}
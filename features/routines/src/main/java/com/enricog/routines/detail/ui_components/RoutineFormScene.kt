package com.enricog.routines.detail.ui_components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.enricog.ui_components.resources.dimensions

internal const val RoutineFormSceneTestTag = "RoutineFormSceneTestTag"

@Composable
internal fun RoutineFormScene() {
    Column(
        modifier = Modifier.fillMaxSize()
            .testTag(RoutineFormSceneTestTag)
            .padding(MaterialTheme.dimensions.spaceM)
    ) {

        TextField("text", onValueChange = {}, isErrorValue = false, label = { Text("field")})

    }
}


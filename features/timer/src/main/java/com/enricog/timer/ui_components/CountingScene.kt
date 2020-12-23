package com.enricog.timer.ui_components

import androidx.compose.animation.core.FloatPropKey
import androidx.compose.animation.core.TransitionDefinition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.transitionDefinition
import androidx.compose.animation.transition
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.AmbientConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.enricog.timer.models.TimerActions
import com.enricog.timer.models.TimerViewState

internal const val CountingSceneTestTag = "CountingSceneTestTag"


@Composable
internal fun CountingScene(state: TimerViewState.Counting, timerActions: TimerActions) {
    val configuration = AmbientConfiguration.current
    val middleScreenOffset = configuration.screenHeightDp / 4

    val count = state.step.count

    val transitionDefinition = remember { completeTransitionDefinition() }
    val transition = transition(
        definition = transitionDefinition,
        toState = state.isRoutineCompleted
    )

    val offset = lerp((-middleScreenOffset).dp, 0.dp, transition[Offset])

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .testTag(CountingSceneTestTag),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(
            stepTitle = stringResource(state.stepTitleId),
            segmentName = state.segmentName,
            modifier = Modifier.alpha(transition[AlphaProp])
        )

        Clock(
            backgroundColor = state.clockBackgroundColor,
            timeInSeconds = count.timeInSeconds,
            modifier =  Modifier
                .scale(transition[ScaleProp])
                .offset(y = offset)
        )
        Spacer(modifier = Modifier.height(40.dp))

        ActionsBar(
            isTimeRunning = count.isRunning,
            isRoutineCompleted = state.isRoutineCompleted,
            timerActions = timerActions,
            modifier = Modifier.offset(y = offset)
        )
    }
}

@Composable
private fun Title(
    stepTitle: String,
    segmentName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stepTitle,
            style = MaterialTheme.typography.h3
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = segmentName,
            style = MaterialTheme.typography.h1.copy(fontWeight = FontWeight.Normal)
        )
        Spacer(modifier = Modifier.height(40.dp))
    }
}


private val AlphaProp = FloatPropKey("Alpha")
private val ScaleProp = FloatPropKey("Scale")
private val Offset = FloatPropKey("Offset")

private fun completeTransitionDefinition(): TransitionDefinition<Boolean> {
    return transitionDefinition {
        state(name = true) { // completed
            this[AlphaProp] = 0f
            this[ScaleProp] = 0.5f
            this[Offset] = 0f
        }
        state(name = false) { // not completed
            this[AlphaProp] = 1f
            this[ScaleProp] = 1f
            this[Offset] = 1f
        }
        transition(fromState = false, toState = true) {
            AlphaProp using spring()
            ScaleProp using spring()
            Offset using spring()
        }
        transition(fromState = true, toState = false) {
            AlphaProp using spring()
            ScaleProp using spring()
            Offset using spring()
        }
    }
}
package com.enricog.features.routines.detail.routine.models

import com.enricog.core.compose.api.classes.Text
import com.enricog.data.routines.api.entities.FrequencyGoal
import com.enricog.features.routines.R
import com.enricog.ui.components.dropDown.TempoDropDownItem

internal fun FrequencyGoal.Period.toDropDownItem(): TempoDropDownItem {
    return TempoDropDownItem(id = name, text = getText())
}

internal fun FrequencyGoal.Period.getText(): Text {
    return when (this) {
        FrequencyGoal.Period.DAY -> Text.Resource(resId = R.string.frequency_goal_period_day_name)

        FrequencyGoal.Period.WEEK -> Text.Resource(resId = R.string.frequency_goal_period_day_week)

        FrequencyGoal.Period.MONTH -> Text.Resource(resId = R.string.frequency_goal_period_day_month)
    }
}

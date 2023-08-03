package com.enricog.data.routines.api.entities

data class FrequencyGoal(
    val times: Int,
    val period: Period
) {

    init {
        require(times >= MIN_TIMES) {
            "times must be equals or grater than $MIN_TIMES"
        }
    }

    enum class Period {
        DAY, WEEK, MONTH
    }

    companion object {
        const val MIN_TIMES = 1
    }
}
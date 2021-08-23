package com.enricog.routines.navigation

internal interface RoutinesNavigationActions {
    fun routinesBack()
    fun goToRoutineSummary(routineId: Long)
    fun goToRoutine(routineId: Long?)
    fun goToSegment(routineId: Long, segmentId: Long?)
    fun goToTimer(routineId: Long)
}
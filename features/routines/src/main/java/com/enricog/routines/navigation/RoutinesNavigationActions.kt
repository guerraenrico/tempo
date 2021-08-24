package com.enricog.routines.navigation

internal interface RoutinesNavigationActions {
    suspend fun routinesBack()
    suspend fun goToRoutineSummary(routineId: Long)
    suspend fun goToRoutine(routineId: Long?)
    suspend fun goToSegment(routineId: Long, segmentId: Long?)
    suspend fun goToTimer(routineId: Long)
}
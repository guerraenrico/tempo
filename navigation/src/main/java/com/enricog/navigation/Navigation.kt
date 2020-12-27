package com.enricog.navigation

interface Navigation {
    fun getRoutinesDeepLink(): String
    fun getRoutineDeepLink(routineId: Long?): String
    fun getTimerDeepLink(routineId: Long): String
}
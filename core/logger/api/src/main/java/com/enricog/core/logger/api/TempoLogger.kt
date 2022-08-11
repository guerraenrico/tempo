package com.enricog.core.logger.api

import android.util.Log
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import timber.log.Timber
import timber.log.Timber.DebugTree

/**
 * Logger to wrap Timber
 * Firebase search -> key:feature value:TAG_TEST
 */
object TempoLogger {

    private val ALLOWED_PRIORITIES = listOf(Log.INFO, Log.ERROR)
    private const val ROUTE_NAME_KEY = "route"

    private var isCrashlyticsPlanted = false

    fun install(isDebug: Boolean) {
        if (isDebug) {
            isCrashlyticsPlanted = false
            Timber.plant(DebugTree())
        } else {
            isCrashlyticsPlanted = true
            Timber.plant(CrashlyticsTree())
        }
    }

    /**
     * Set a crashlytics custom key with the last routine name
     * that it will be apply in the next crash
     */
    fun setRouteName(routeName: String) {
        if (isCrashlyticsPlanted) {
            Firebase.crashlytics.setCustomKey(ROUTE_NAME_KEY, routeName)
        }
    }

    fun d(throwable: Throwable, message: String) {
        Timber.d(throwable, message)
    }

    fun d(throwable: Throwable) {
        Timber.d(throwable)
    }

    fun i(message: String) {
        Timber.i(message)
    }

    fun e(throwable: Throwable, message: String) {
        Timber.e(throwable, message)
    }

    fun e(throwable: Throwable) {
        Timber.e(throwable)
    }

    private class CrashlyticsTree : Timber.Tree() {

        override fun isLoggable(tag: String?, priority: Int): Boolean {
            return ALLOWED_PRIORITIES.contains(priority)
        }

        override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
            if (t == null) {
                Firebase.crashlytics.log(message)
            }
            if (t != null) {
                Firebase.crashlytics.recordException(t)
            }
        }
    }
}
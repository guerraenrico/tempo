package com.enricog.base.extensions

@Deprecated(message = "Not necessary anymore after kotlin 1.7", replaceWith = ReplaceWith(""))
val <T> T.exhaustive: T
    get() = this

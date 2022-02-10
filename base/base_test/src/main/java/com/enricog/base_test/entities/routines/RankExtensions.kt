package com.enricog.base_test.entities.routines

import com.enricog.entities.Rank

val Rank.Companion.RANDOM: Rank
    get() = from("abcdef")

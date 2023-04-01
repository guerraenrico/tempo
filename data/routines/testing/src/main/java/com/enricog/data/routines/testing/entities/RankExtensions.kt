package com.enricog.data.routines.testing.entities

import com.enricog.core.entities.Rank

val Rank.Companion.RANDOM: Rank
    get() = from("abcdef")

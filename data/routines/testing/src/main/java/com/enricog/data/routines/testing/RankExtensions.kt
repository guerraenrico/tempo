package com.enricog.data.routines.testing

import com.enricog.entities.Rank

val Rank.Companion.RANDOM: Rank
    get() = from("abcdef")
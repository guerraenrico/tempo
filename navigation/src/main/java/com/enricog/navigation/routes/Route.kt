package com.enricog.navigation.routes

import androidx.navigation.NavArgumentBuilder

interface Route {
    val routeName: String
    val arguments: List<Pair<String, NavArgumentBuilder.() -> Unit>>
    val deepLinkPatterns: List<String>
}
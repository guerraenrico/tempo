@file:Suppress("unused")
package com.enricog.tempo.buildsrc

object Versions {
    const val compileSdk = 30
    const val minSdk = 24
    const val targetSdk = 30
}

object Libs {

    object Kotlin {
        const val version = "1.4.20"
        private const val coroutineVersion = "1.4.2"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"

        object Test {
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion"
            const val kotlin = "org.jetbrains.kotlin:kotlin-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.3.2"

        const val appCompat = "androidx.appcompat:appcompat:1.2.0"

        const val fragmentKtx = "androidx.fragment:fragment-ktx:1.3.0-beta01"
        const val activityKtx = "androidx.activity:activity-ktx:1.2.0-beta01"

        object Compose {
            const val version = "1.0.0-alpha08"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"

            const val material = "androidx.compose.material:material:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"

            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"

            const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha01"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.2.0-beta01"
    }

    object Lifecycle {
        private const val version = "2.2.0"

        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
    }

    object Hilt {
        const val androidVersion = "2.28.3-alpha"
        private const val version = "1.0.0-alpha02"

        const val android = "com.google.dagger:hilt-android:$androidVersion"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$androidVersion"

        const val lifecycleViewModel = "androidx.hilt:hilt-lifecycle-viewmodel:$version"
        const val compiler = "androidx.hilt:hilt-compiler:$version"
        const val work = "androidx.hilt:hilt-work:$version"
    }

    object Room {
        private const val version = "2.2.5"

        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
    }

    const val Timber = "com.jakewharton.timber:timber:4.7.1"

    object Lint {
        private const val version = "26.4.1"

        const val api = "com.android.tools.lint:lint-api:$version"
        const val checks = "com.android.tools.lint:lint-checks:$version"
    }

    // Test

    object Test {
        const val junit = "junit:junit:4.13"
        const val junitKtx = "androidx.test.ext:junit-ktx:1.1.2-rc01"

        private const val version = "1.2.0"
        const val core = "androidx.test:core:$version"
        const val rules = "androidx.test:rules:$version"
    }

    const val Robolectric = "org.robolectric:robolectric:4.3.1"

    object Mockk {
        private const val version = "1.10.0"

        const val lib = "io.mockk:mockk:$version"
        const val android = "io.mockk:mockk-android:$version"
    }
}
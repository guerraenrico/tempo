@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.enricog.tempo.buildsrc

object Versions {
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0
    private const val versionBuild = 2

    const val appVersionCode: Int =
        versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100 + versionBuild
    const val appVersionName: String = "$versionMajor.$versionMinor.$versionPatch"

    const val compileSdk = 30
    const val minSdk = 24
    const val targetSdk = 30

    const val jvmTarget = "1.8"
}

object Libs {

    object Kotlin {
        const val version = "1.4.32"
        private const val coroutineVersion = "1.4.3"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object Test {
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion"
            const val kotlin = "org.jetbrains.kotlin:kotlin-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.3.2"

        const val appCompat = "androidx.appcompat:appcompat:1.2.0"

        object Test {
            const val junitKtx = "androidx.test.ext:junit-ktx:1.1.2"

            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
            const val archCore = "androidx.arch.core:core-testing:2.1.0"
        }

        object Compose {
            const val version = "1.0.0-beta06"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"

            const val material = "androidx.compose.material:material:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"

            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"

            const val activity = "androidx.activity:activity-compose:1.3.0-alpha07"

            const val navigation = "androidx.navigation:navigation-compose:1.0.0-alpha10"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha04"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-alpha06"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.2.0"
    }

    object Lifecycle {
        private const val version = "2.3.1"

        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
    }

    object Hilt {
        const val androidVersion = "2.35.1"
        private const val version = "1.0.0-beta01"

        const val android = "com.google.dagger:hilt-android:$androidVersion"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$androidVersion"

        const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0-alpha01"

        const val work = "androidx.hilt:hilt-work:$version"
    }

    object Room {
        private const val version = "2.3.0"

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

    object Tools {
        const val desugar = "com.android.tools:desugar_jdk_libs:1.1.5"
    }

    object Firebase {
        private const val version = "28.0.0"

        const val bom = "com.google.firebase:firebase-bom:$version"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
    }

    // Test

    object Test {
        const val junit = "junit:junit:4.12"
        const val flow = "app.cash.turbine:turbine:0.4.1"
    }

    const val Robolectric = "org.robolectric:robolectric:4.3.1"

    object Mockk {
        private const val version = "1.11.0"

        const val lib = "io.mockk:mockk:$version"
        const val android = "io.mockk:mockk-android:$version"
    }
}
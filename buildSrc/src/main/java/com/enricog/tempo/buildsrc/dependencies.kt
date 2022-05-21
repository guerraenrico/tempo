@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.enricog.tempo.buildsrc

object Versions {
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 0
    private const val versionBuild = 3

    const val appVersionCode: Int =
        versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100 + versionBuild
    const val appVersionName: String = "$versionMajor.$versionMinor.$versionPatch"

    const val compileSdk = 31
    const val minSdk = 24
    const val targetSdk = 31

    const val jvmTarget = "1.8"
}

object Libs {

    object Kotlin {
        const val version = "1.6.10"
        private const val coroutineVersion = "1.6.0"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object Test {
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion"
            const val kotlin = "org.jetbrains.kotlin:kotlin-test:$version"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        const val appCompat = "androidx.appcompat:appcompat:1.4.1"

        object Test {
            const val junitKtx = "androidx.test.ext:junit-ktx:1.1.3"

            private const val version = "1.4.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
            const val archCore = "androidx.arch.core:core-testing:2.1.0"
        }

        object Compose {
            const val version = "1.1.1"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val layout = "androidx.compose.foundation:foundation-layout:$version"

            const val material = "androidx.compose.material:material:$version"
            const val ui = "androidx.compose.ui:ui:$version"
            const val tooling = "androidx.compose.ui:ui-tooling:$version"
            const val uiUtil = "androidx.compose.ui:ui-util:${version}"
            const val animation = "androidx.compose.animation:animation:${version}"

            const val test = "androidx.compose.ui:ui-test:$version"
            const val uiTest = "androidx.compose.ui:ui-test-junit4:$version"

            const val activity = "androidx.activity:activity-compose:1.3.1"

            const val navigation = "androidx.navigation:navigation-compose:2.4.2"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.4.1"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.2.0"
    }

    object Lifecycle {
        private const val version = "2.4.1"

        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
    }

    object Hilt {
        const val androidVersion = "2.41"
        private const val version = "1.0.0"

        const val android = "com.google.dagger:hilt-android:$androidVersion"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$androidVersion"
        const val compiler = "androidx.hilt:hilt-compiler:$version"

        const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"

        const val work = "androidx.hilt:hilt-work:$version"
    }

    object Room {
        private const val version = "2.4.2"

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
        private const val version = "30.0.2"

        const val bom = "com.google.firebase:firebase-bom:$version"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
    }

    // Test

    object Test {
        private const val junit5 = "1.8.2.0"
        private const val junitJupiter = "5.8.2"

        const val junitGradlePlugin = "de.mannodermaus.gradle.plugins:android-junit5:$junit5"
        const val junitJupiterApi = "org.junit.jupiter:junit-jupiter-api:$junitJupiter"
        const val junitJupiterEngine = "org.junit.jupiter:junit-jupiter-engine:$junitJupiter"
        const val junitJupiterParams = "org.junit.jupiter:junit-jupiter-params:$junitJupiter"
        const val junit = "junit:junit:4.12"
        const val flow = "app.cash.turbine:turbine:0.8.0"
    }

    const val Robolectric = "org.robolectric:robolectric:4.3.1"
}
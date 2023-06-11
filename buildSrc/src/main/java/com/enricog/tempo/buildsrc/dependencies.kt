@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.enricog.tempo.buildsrc

object Versions {
    private const val versionMajor = 1
    private const val versionMinor = 0
    private const val versionPatch = 3
    private const val versionBuild = 1

    const val appVersionCode: Int =
        versionMajor * 1000000 + versionMinor * 10000 + versionPatch * 100 + versionBuild
    const val appVersionName: String = "$versionMajor.$versionMinor.$versionPatch"

    const val compileSdk = 33
    const val minSdk = 28
    const val targetSdk = 33

    const val jvmTarget = "1.8"
}

object Libs {

    object Kotlin {
        const val version = "1.8.21"
        private const val coroutineVersion = "1.7.1"
        private const val serializationVersion = "1.5.1"

        const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutineVersion"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$version"
        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion"

        object Test {
            const val coroutines = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutineVersion"
        }
    }

    object AndroidX {
        const val coreKtx = "androidx.core:core-ktx:1.9.0"

        const val appCompat = "androidx.appcompat:appcompat:1.5.1"

        object Test {
            const val junitKtx = "androidx.test.ext:junit-ktx:1.1.5"

            private const val version = "1.5.0"
            const val core = "androidx.test:core:$version"
            const val rules = "androidx.test:rules:$version"
        }

        object Compose {
            const val compilerVersion = "1.4.7"
            private const val version = "2023.05.01"

            const val bom = "androidx.compose:compose-bom:$version"

            const val runtime = "androidx.compose.runtime:runtime"
            const val foundation = "androidx.compose.foundation:foundation"
            const val layout = "androidx.compose.foundation:foundation-layout"

            const val material = "androidx.compose.material:material"
            const val ui = "androidx.compose.ui:ui"
            const val tooling = "androidx.compose.ui:ui-tooling"
            const val util = "androidx.compose.ui:ui-util"
            const val animation = "androidx.compose.animation:animation"

            const val test = "androidx.compose.ui:ui-test"
            const val uiTest = "androidx.compose.ui:ui-test-junit4"

            const val activity = "androidx.activity:activity-compose:1.6.1"

            const val navigation = "androidx.navigation:navigation-compose:2.5.3"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1"
            const val constraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.1"
        }
    }

    object Google {
        const val material = "com.google.android.material:material:1.2.0"

        object Accompanist {
            private const val version = "0.30.1"

            const val navigation = "com.google.accompanist:accompanist-navigation-material:$version"
            const val permission = "com.google.accompanist:accompanist-permissions:$version"
        }
    }

    object Lifecycle {
        private const val version = "2.6.1"

        const val viewModelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        const val commonJava8 = "androidx.lifecycle:lifecycle-common-java8:$version"
        const val compose = "androidx.lifecycle:lifecycle-runtime-compose:$version"
    }

    object Hilt {
        const val androidVersion = "2.46.1"
        private const val version = "1.0.0"

        const val android = "com.google.dagger:hilt-android:$androidVersion"
        const val androidCompiler = "com.google.dagger:hilt-android-compiler:$androidVersion"
        const val compiler = "androidx.hilt:hilt-compiler:$version"

        const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"

        const val work = "androidx.hilt:hilt-work:$version"
    }

    object Room {
        private const val version = "2.5.0"

        const val runtime = "androidx.room:room-runtime:$version"
        const val compiler = "androidx.room:room-compiler:$version"
        const val ktx = "androidx.room:room-ktx:$version"
        const val testing = "androidx.room:room-testing:$version"
    }

    object DataStore {
        private const val version = "1.0.0"

        const val preferences = "androidx.datastore:datastore-preferences:$version"
    }

    const val Timber = "com.jakewharton.timber:timber:5.0.1"

    object Lint {
        private const val version = "26.4.1"

        const val api = "com.android.tools.lint:lint-api:$version"
        const val checks = "com.android.tools.lint:lint-checks:$version"
    }

    object Firebase {
        private const val version = "31.2.3"

        const val bom = "com.google.firebase:firebase-bom:$version"
        const val crashlytics = "com.google.firebase:firebase-crashlytics-ktx"
        const val analytics = "com.google.firebase:firebase-analytics-ktx"
    }

    object Test {
        const val junit = "junit:junit:4.12"
        const val parameterInjector = "com.google.testparameterinjector:test-parameter-injector:1.10"
        const val truth = "com.google.truth:truth:1.1.3"

        const val flow = "app.cash.turbine:turbine:0.12.0"
    }
}
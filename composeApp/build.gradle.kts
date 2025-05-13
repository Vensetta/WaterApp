import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSetTree

plugins {
    alias(libs.plugins.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
    alias(libs.plugins.buildConfig)
}

kotlin {
    jvmToolchain(21)
    androidTarget {
        //https://www.jetbrains.com/help/kotlin-multiplatform-dev/compose-test.html
        instrumentedTestVariant.sourceSetTree.set(KotlinSourceSetTree.test)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.kermit)
            implementation(libs.kotlinx.coroutines.core)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.auth)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.client.serialization)
            implementation(libs.ktor.serialization.kotlinx.json)
            implementation(libs.ktor.client.logging)

            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)

            implementation(libs.decompose)
            implementation(libs.decompose.compose)

            implementation(libs.mvi)
            implementation(libs.mvi.main)
            implementation(libs.mvi.coroutines)
            implementation(libs.mvi.logging)

            implementation(libs.kotlinx.serialization.json)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)

            implementation(libs.coil)
            implementation(libs.coil.network.ktor)

            implementation(libs.multiplatformSettings)
            implementation(libs.multiplatformSettings.coroutines)
            implementation(libs.multiplatformSettings.no.arg)

            implementation(libs.kotlinx.datetime)

            implementation(libs.room.runtime)
            implementation(libs.sqllite.bundled)

            implementation(libs.kstore)

            implementation(libs.materialKolor)
            implementation(libs.composeIcons.featherIcons)
            implementation(libs.koalaPlot.core)

            implementation(libs.oidc.appsupport)
            implementation(libs.oidc.ktor)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.uiTest)
            implementation(libs.kotlinx.coroutines.test)
        }

        androidMain.dependencies {
            implementation(compose.uiTooling)
            implementation(libs.androidx.activityCompose)
            implementation(libs.kotlinx.coroutines.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.oidc.okhttp4)
            implementation(libs.koin.android)
        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }

    }
}

android {
    namespace = "org.turter.water_app_mobile"
    compileSdk = 35

    defaultConfig {
        minSdk = 23
        targetSdk = 35

        applicationId = "org.turter.water_app_mobile.androidApp"
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        addManifestPlaceholders(
            mapOf("oidcRedirectScheme" to "water.app.mobile://callback")
        )
    }
}

//https://developer.android.com/develop/ui/compose/testing#setup
dependencies {
    androidTestImplementation(libs.androidx.uitest.junit4)
    debugImplementation(libs.androidx.uitest.testManifest)
}

buildConfig {
    // BuildConfig configuration here.
    // https://github.com/gmazzo/gradle-buildconfig-plugin#usage-in-kts
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    with(libs.room.compiler) {
        add("kspAndroid", this)
        add("kspIosX64", this)
        add("kspIosArm64", this)
        add("kspIosSimulatorArm64", this)
    }
}

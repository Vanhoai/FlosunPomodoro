import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.google.services)
}

val keystoreFile: File = rootProject.file("keystore.properties")
if (!keystoreFile.exists()) {
    throw GradleException("Keystore file not found: ${keystoreFile.absolutePath}")
}

val keystoreProperties = Properties()
if (keystoreFile.exists()) keystoreProperties.load(keystoreFile.inputStream())

val localPropertiesFile: File = rootProject.file("local.properties")
if (!localPropertiesFile.exists()) {
    throw GradleException("Local properties file not found: ${localPropertiesFile.absolutePath}")
}

val localProperties = Properties()
if (localPropertiesFile.exists()) localProperties.load(localPropertiesFile.inputStream())

// Require versionCode and versionName in local.properties
if (!localProperties.containsKey("versionCode") || !localProperties.containsKey("versionName"))
    throw GradleException("versionCode and versionName must be defined in local.properties")

fun formatKeyBuildConfig(key: String): String {
    val keys = mutableListOf<String>()
    var s = ""

    for (c in key.toCharArray()) {
        if (c.isUpperCase()) {
            keys.add(s)
            s = c.toString()
        } else s += c
    }

    keys.add(s)
    return keys.joinToString("_").uppercase(Locale.ROOT)
}

fun parseLocalPropertiesToBuildConfig(buildConfigField: (type: String, name: String, value: String) -> Unit) {
    // Parse local.properties and add all keys as BuildConfig fields
    // noinspection WrongGradleMethod
    localProperties.propertyNames().asIterator().forEach { propName ->
        if (propName.toString() == "sdk.dir") return@forEach

        buildConfigField(
            "String",
            formatKeyBuildConfig(propName.toString()),
            "\"${localProperties.getProperty(propName.toString())}\""
        )
    }
}

android {
    namespace = "com.flosun.pomodoro"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.flosun.pomodoro"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 36
        versionCode = localProperties["versionCode"].toString().toInt()
        versionName = localProperties["versionName"].toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            // abiFilters += setOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            // noinspection ChromeOsAbiSupport
            abiFilters += setOf("arm64-v8a")
        }
    }

    signingConfigs {
        create("release") {
            storeFile = file(keystoreProperties["storeFile"].toString())
            storePassword = keystoreProperties["storePassword"].toString()
            keyAlias = keystoreProperties["keyAlias"].toString()
            keyPassword = keystoreProperties["keyPassword"].toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            signingConfig = signingConfigs.getByName("release")
        }

        debug {
            isMinifyEnabled = false

            // Use my own config
            signingConfig = signingConfigs.getByName("release")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        compose = true
    }

    sourceSets {
        named("main") {
            kotlin {
                directories += "additionalSourceDirectory/kotlin"
            }
        }
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Modules
    implementation(project(":core"))

    // Libraries
    implementation(files("../libraries/shared.aar"))

    // Navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Room Database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    testImplementation(libs.androidx.room.testing)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // Biometrics
    implementation(libs.biometric)

    // Timber
    implementation(libs.timber)

    // Coil for image loading
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    // Glance
    // implementation(libs.glance.appwidget)
    // implementation(libs.glance.material3)

    // Google Auth
    implementation(libs.credentials)
    implementation(libs.credentials.play.services.auth)
    implementation(libs.googleid)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)

    // Media3
    implementation(libs.media3.common)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)
    implementation(libs.media3.session)

    // Supabase
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.supabase.auth)
    implementation(libs.supabase.realtime)

    // Ktor
    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.android)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)

    // Datastore
    implementation(libs.datastore)

    // Maps
    implementation(libs.maps.compose)
    implementation(libs.maplibre.compose)

    // Location
    implementation(libs.play.services.location)
}
import java.util.Locale
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
}

val localPropertiesFile: File = rootProject.file("local.properties")
if (!localPropertiesFile.exists()) {
    throw GradleException("Local properties file not found: ${localPropertiesFile.absolutePath}")
}

val localProperties = Properties()
if (localPropertiesFile.exists()) localProperties.load(localPropertiesFile.inputStream())

// Require datastoreName in local.properties
if (!localProperties.containsKey("datastoreKey"))
    throw GradleException("datastoreKey must be defined in local.properties")

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
    namespace = "com.flosunn.core"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            parseLocalPropertiesToBuildConfig(buildConfigField = ::buildConfigField)
        }

        debug {
            isMinifyEnabled = false
            parseLocalPropertiesToBuildConfig(buildConfigField = ::buildConfigField)
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    sourceSets {
        named("main") {
            kotlin {
                directories += "additionalSourceDirectory/kotlin"
            }
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

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

    // Kotlinx Datetime
    implementation(libs.kotlinx.datetime)

    // Hilt Dependency Injection
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)

    // Datastore
    implementation(libs.datastore)

    // Timber
    implementation(libs.timber)
}
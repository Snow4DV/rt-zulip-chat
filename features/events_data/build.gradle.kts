plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.events_impl"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()

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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        }
    kotlinOptions {
        jvmTarget = "17"
    }
}


dependencies {
    // Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Core
    implementation(projects.core.moduleInjector)
    implementation(projects.core.utils)

    // Libs
    implementation(projects.lib.network)
    implementation(projects.lib.authStorage)

    // Domain api
    implementation(projects.features.eventsDomainApi)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Network
    implementation(projects.lib.network)
}
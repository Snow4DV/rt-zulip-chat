plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.events_impl"
    compileSdk = 34

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
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    // Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Core
    implementation(projects.core.moduleInjector)

    // Libs
    implementation(projects.lib.network)
    implementation(projects.lib.authStorage)

    // Domain api
    implementation(projects.features.eventsDomainApi)


    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.utils)

    implementation(projects.lib.network)
}
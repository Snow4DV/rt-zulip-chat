plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.androidX.room)
    alias(libs.plugins.kotlinSerialization)
}

android {
    namespace = "ru.snowadv.database"
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
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.moduleInjector)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.androidX.room.runtime)
    kapt(libs.androidX.room.compiler)
    implementation(libs.androidX.room.ktx)

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinSerialization.json)
}
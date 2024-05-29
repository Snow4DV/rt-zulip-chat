plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.image_loader"
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
    implementation(projects.core.moduleInjector)
    implementation(projects.core.utils)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.coil)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit.core)
}
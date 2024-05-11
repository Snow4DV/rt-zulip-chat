plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.auth_data"
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Database & network
    implementation(projects.lib.authStorage)

    // Domain repo api
    implementation(projects.features.authDomainApi)

    // Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Utils and injector
    implementation(projects.core.utils)
    implementation(projects.core.moduleInjector)

    // Coroutines
    implementation(libs.androidx.core.ktx)

    // Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.users_data"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // Database & network
    implementation(projects.lib.database)
    implementation(projects.lib.network)
    implementation(projects.lib.authStorage)

    // Domain repo api
    implementation(projects.features.usersDomainApi)

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
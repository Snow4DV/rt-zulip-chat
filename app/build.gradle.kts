plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.voiceapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.snowadv.voiceapp"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // New multi-layer features

    // Auth feature
    implementation(projects.features.authData)
    implementation(projects.features.authDomainApi)
    implementation(projects.features.authDomainImpl)
    implementation(projects.features.authPresentation)
    // Chat feature
    implementation(projects.features.chatData)
    implementation(projects.features.chatDomainApi)
    implementation(projects.features.chatDomainImpl)
    implementation(projects.features.chatPresentation)
    // Events feature
    implementation(projects.features.eventsData)
    implementation(projects.features.eventsDomainApi)

    // Old data TODO: Refactor
    implementation(projects.data.channelsData.api)
    implementation(projects.data.channelsData.impl)
    implementation(projects.features.eventsDomainApi)
    implementation(projects.data.usersData.api)
    implementation(projects.data.usersData.impl)

    // Old features TODO: Refactor
    implementation(projects.features.channels.api)
    implementation(projects.features.channels.impl)
    implementation(projects.features.home.api)
    implementation(projects.features.home.impl)
    implementation(projects.features.people.api)
    implementation(projects.features.people.impl)
    implementation(projects.features.profile.api)
    implementation(projects.features.profile.impl)

    // Core
    implementation(projects.core.utils)
    implementation(projects.core.moduleInjector)

    // Libs
    implementation(projects.lib.network)
    implementation(projects.lib.networkAuthorizer)
    implementation(projects.lib.database)
    implementation(projects.lib.imageLoader)
    implementation(projects.lib.authStorage)



    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.kotlinSerialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.cicerone)
    implementation(libs.retrofit.core)
    implementation(libs.coil)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
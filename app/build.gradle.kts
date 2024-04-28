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
    implementation(projects.data.channelsData.api)
    implementation(projects.data.channelsData.impl)
    implementation(projects.data.eventsData.api)
    implementation(projects.data.eventsData.impl)
    implementation(projects.data.messagesData.api)
    implementation(projects.data.messagesData.impl)
    implementation(projects.data.usersData.api)
    implementation(projects.data.usersData.impl)
    implementation(projects.data.emojisData.api)
    implementation(projects.data.emojisData.impl)
    implementation(projects.data.authData.api)
    implementation(projects.data.authData.impl)

    implementation(projects.features.channels)
    implementation(projects.features.chat)
    implementation(projects.features.home)
    implementation(projects.features.people)
    implementation(projects.features.profile)

    implementation(projects.core.utils)
    implementation(projects.core.data)
    implementation(projects.core.network)

    implementation(projects.core.propertiesProvider.api)
    implementation(projects.core.propertiesProvider.impl)

    implementation(projects.core.moduleInjector)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.kotlinSerialization.json)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.cicerone)
    api(projects.navigation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
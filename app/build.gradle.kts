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
    implementation(projects.features.events.api)
    implementation(projects.features.events.impl)
    implementation(projects.data.messagesData.api)
    implementation(projects.data.messagesData.impl)
    implementation(projects.data.usersData.api)
    implementation(projects.data.usersData.impl)
    implementation(projects.data.emojisData.api)
    implementation(projects.data.emojisData.impl)
    implementation(projects.data.authData.api)
    implementation(projects.data.authData.impl)

    implementation(projects.features.channels.api)
    implementation(projects.features.channels.impl)
    implementation(projects.features.chat.api)
    implementation(projects.features.chat.impl)
    implementation(projects.features.home.api)
    implementation(projects.features.home.impl)
    implementation(projects.features.people.api)
    implementation(projects.features.people.impl)
    implementation(projects.features.profile.api)
    implementation(projects.features.profile.impl)
    implementation(projects.features.auth.api)
    implementation(projects.features.auth.impl)

    implementation(projects.core.utils)

    implementation(projects.lib.network)
    implementation(projects.lib.networkAuthorizer)
    implementation(projects.lib.database)

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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
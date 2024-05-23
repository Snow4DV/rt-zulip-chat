plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.chatapp"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.snowadv.chatapp"
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        viewBinding = true
    }

    testOptions {
        animationsDisabled = true
    }
}

dependencies {
    // Andriod Test Rules
    androidTestImplementation(libs.androidx.test.rules)
    // JUnit
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Kotest
    testImplementation(libs.kotest.junit)
    testImplementation(libs.kotest.assertions)
    testImplementation(libs.kotest.property)



    // Hamcrest Matchers
    androidTestImplementation(libs.hamcrest)

    // Kaspresso
    androidTestImplementation(libs.kaspresso)

    // Espresso Intents
    androidTestImplementation(libs.androidx.espresso.intents)

    // Wiremock
    debugImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.httpclient.android)
    androidTestImplementation(libs.wiremock) {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
    }

    // Test utils
    testImplementation(projects.core.testUtils)

    // Instrument tests
    implementation(libs.androidx.fragment.testing)

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
    // Channels feature
    implementation(projects.features.channelsData)
    implementation(projects.features.channelsDomainApi)
    implementation(projects.features.channelsDomainImpl)
    implementation(projects.features.channelsPresentation)
    // Users feature
    implementation(projects.features.usersData)
    implementation(projects.features.usersDomainApi)
    implementation(projects.features.usersDomainImpl)
    // People feature
    implementation(projects.features.peoplePresentation)
    // Profile feature
    implementation(projects.features.profilePresentation)
    // Home feature
    implementation(projects.features.homePresentation)


    // Core
    implementation(projects.core.utils)
    implementation(projects.core.moduleInjector)
    implementation(projects.core.presentation)

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
    
}
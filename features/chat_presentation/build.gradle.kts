plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "ru.snowadv.chat_presentation"
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

    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
    }
    testOptions {
        unitTests.all { it.useJUnitPlatform() }
        animationsDisabled = true
    }
}

dependencies {
    // Coil
    implementation(libs.coil)

    // Markwon
    implementation(libs.markwon.core)
    implementation(libs.markwon.latex)
    implementation(libs.markwon.coil)
    implementation(libs.markwon.tables)
    implementation(libs.markwon.strikethrough)
    implementation(libs.markwon.html)

    // Presentation dependencies
    implementation(projects.features.messageActionsPresentation)

    // Feature domain api
    implementation(projects.features.chatDomainApi)
    implementation(projects.features.eventsDomainApi)
    implementation(projects.features.channelsDomainApi) // To get topics in chat screen

    // Feature basic core deps
    implementation(projects.core.utils)
    implementation(projects.core.presentation)
    implementation(projects.core.moduleInjector)

    // Dagger
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    // Elmslie
    implementation(libs.elmslie.core)
    implementation(libs.elmslie.android)

    // Facebook shimmer
    implementation(libs.facebook.shimmer)

    // Presentation deps
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.fragment.ktx)
    

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
    androidTestImplementation(libs.androidx.fragment.testing)
}
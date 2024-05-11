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
    // Coil
    implementation(libs.coil)

    // Markwon
    implementation(libs.markwon.core)
    implementation(libs.markwon.latex)
    implementation(libs.markwon.coil)
    implementation(libs.markwon.tables)
    implementation(libs.markwon.strikethrough)
    implementation(libs.markwon.html)

    // Feature domain api
    implementation(projects.features.chatDomainApi)
    implementation(projects.features.eventsDomainApi)

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
    coreLibraryDesugaring(libs.desugar.jdk.libs)
}
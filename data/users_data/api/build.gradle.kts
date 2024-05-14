plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "ru.snowadv.users_data_api"
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
}


dependencies {
    implementation(projects.core.moduleInjector)
    implementation(projects.core.utils)
    

    implementation(projects.lib.network)
    implementation(projects.lib.database)
    implementation(projects.lib.authStorage)

    implementation(libs.kotlinx.coroutines.core)

    
}
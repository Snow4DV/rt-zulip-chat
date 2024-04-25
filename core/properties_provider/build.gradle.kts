import java.util.Properties

plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "ru.snowadv.properties_provider"
    compileSdk = 34

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")


        val localPropertiesFile = File(project.rootDir, "local.properties")
        val localProperties = Properties().apply {
            if (localPropertiesFile.exists()) {
                load(localPropertiesFile.inputStream())
            } else {
                error("Local properties file not found. Check if it exists in root project folder")
            }
        }

        val actionIfSomethingIsMissingInProps: (String) -> Nothing = { error("Sit is missing in local.properties file. Please add it :)") }

        buildConfigField(
            "String",
            "API_KEY",
            localProperties.getProperty("API_KEY", null)?.let { "\"$it\"" }
                ?: actionIfSomethingIsMissingInProps("API_KEY")
        )

        buildConfigField(
            "Long",
            "USER_ID",
            localProperties.getProperty("USER_ID", null)?.let { "${it}L" }
                ?: actionIfSomethingIsMissingInProps("USER_ID")
        )

        buildConfigField(
            "String",
            "USER_EMAIL",
            localProperties.getProperty("USER_EMAIL", null)?.let { "\"$it\"" }
                ?: actionIfSomethingIsMissingInProps("USER_EMAIL")
        )

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
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
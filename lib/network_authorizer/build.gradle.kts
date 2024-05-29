plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlinSerialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(projects.core.moduleInjector)
    implementation(projects.core.utils)

    implementation(libs.dagger)
    kapt(libs.dagger.compiler)

    implementation(libs.kotlinSerialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinxConverter)
    implementation(libs.retrofit.resultConverter)

    implementation(libs.okhttp.logging.interceptor)
}
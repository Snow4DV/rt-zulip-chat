plugins {
    id("java-library")
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.kotlinSerialization)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.kotlinSerialization.json)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlinxConverter)
    implementation(libs.retrofit.resultConverter)
    implementation(projects.core.data)
    implementation(libs.okhttp.logging.interceptor)
}
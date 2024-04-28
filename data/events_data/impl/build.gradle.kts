plugins {
    id(libs.plugins.java.library.get().pluginId)
    alias(libs.plugins.jetbrainsKotlinJvm)
    alias(libs.plugins.kotlin.kapt)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(libs.dagger)
    kapt(libs.dagger.compiler)
    implementation(projects.core.moduleInjector)
    implementation(projects.data.eventsData.api)

    implementation(libs.kotlinx.coroutines.core)
    implementation(projects.core.utils)
    implementation(projects.core.network)
    implementation(projects.core.data)
}
plugins {
    id(libs.plugins.java.library.get().pluginId)
    alias(libs.plugins.jetbrainsKotlinJvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    implementation(projects.core.moduleInjector)
    implementation(projects.core.propertiesProvider.api)
}
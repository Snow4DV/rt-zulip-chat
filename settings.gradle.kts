pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "VoiceApp"
include(":app")
include(":app_contacts")
include(":core")
include(":libraries:contacts-provider")
include(":core:contacts-provider")
include(":features")
include(":features:chat")
include(":core:domain")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":core:presentation")

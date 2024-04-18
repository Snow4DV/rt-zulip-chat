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
include(":core:data")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":core:presentation")
include(":features:home")
include(":core:utils")
include(":navigation")
include(":features:channels")
include(":features:people")
include(":features:profile")
include(":data")
include(":data:channels_data")
include(":core:network")
include(":data:messages_data")
include(":data:events_data")
include(":data:users_data")
include(":data:emojis_data")

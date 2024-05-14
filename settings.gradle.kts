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

        maven("https://jitpack.io")
    }
}

rootProject.name = "ChatApp"
include(":app")
include(":app_contacts")
include(":core")
include(":libraries:contacts-provider")
include(":core:contacts_provider")
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
include(":data:auth_data")
include(":core:properties_provider")
include(":core:module_injector")
include(":data:auth_data:api")
include(":data:auth_data:impl")
include(":core:properties_provider:api")
include(":core:properties_provider:impl")
include(":data:channels_data:api")
include(":data:channels_data:impl")
include(":data:emojis_data:api")
include(":data:emojis_data:impl")
include(":data:events_data:api")
include(":data:events_data:impl")
include(":data:messages_data:api")
include(":data:messages_data:impl")
include(":data:users_data:api")
include(":data:users_data:impl")
include(":features:profile:api")
include(":features:profile:impl")
include(":features:people:api")
include(":features:people:impl")
include(":features:home:api")
include(":features:home:impl")
include(":features:chat:api")
include(":features:chat:impl")
include(":features:channels:api")
include(":features:channels:impl")
include(":features:events")
include(":features:events:api")
include(":features:events:impl")

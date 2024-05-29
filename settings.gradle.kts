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
include(":core")
include(":libraries:contacts-provider")
include(":features")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":core:presentation")
include(":core:utils")
include(":data:events_data")
include(":core:module_injector")
include(":data:events_data:api")
include(":data:events_data:impl")
include(":features:events_data")
include(":features:events_domain_api")
include(":lib")
include(":lib:network")
include(":lib:database")
include(":lib:network_authorizer")
include(":lib:image_loader")
include(":features:chat_data")
include(":features:auth_domain_api")
include(":features:auth_presentation")
include(":features:auth_domain_impl")

include(":features:chat_domain_impl")
include(":features:auth_data")
include(":features:chat_presentation")
include(":features:chat_domain_api")
include(":lib:auth_storage")
include(":core:test_utils")
include(":features:channels_domain_api")
include(":features:channels_domain_impl")
include(":features:channels_data")
include(":features:channels_presentation")
include(":features:users_domain_api")
include(":features:users_domain_impl")
include(":features:users_data")
include(":features:people_presentation")
include(":features:profile_presentation")
include(":features:home_presentation")
include(":features:message_actions_presentation")

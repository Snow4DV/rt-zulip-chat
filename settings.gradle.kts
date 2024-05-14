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
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":core:presentation")
include(":features:home")
include(":core:utils")
include(":features:channels")
include(":features:people")
include(":features:profile")
include(":data")
include(":data:channels_data")
include(":data:events_data")
include(":data:users_data")
include(":core:module_injector")
include(":data:channels_data:api")
include(":data:channels_data:impl")
include(":data:events_data:api")
include(":data:events_data:impl")
include(":data:users_data:api")
include(":data:users_data:impl")
include(":features:profile:api")
include(":features:profile:impl")
include(":features:people:api")
include(":features:people:impl")
include(":features:home:api")
include(":features:home:impl")
include(":features:channels:api")
include(":features:channels:impl")
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

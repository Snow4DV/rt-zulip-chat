package ru.snowadv.voiceapp.glue.injector

import android.content.Context
import kotlinx.serialization.json.Json
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_api.di.AuthDataModuleAPI
import ru.snowadv.auth_data_api.di.AuthDataModuleDependencies
import ru.snowadv.auth_data_impl.di.AuthDataModuleComponentHolder
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.channels_data_api.di.ChannelsDataModuleAPI
import ru.snowadv.channels_data_api.di.ChannelsDataModuleDependencies
import ru.snowadv.channels_data_impl.di.ChannelsDataModuleComponentHolder
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleAPI
import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleDependencies
import ru.snowadv.emojis_data_impl.di.EmojisDataModuleComponentHolder
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.events_data_api.di.EventsDataModuleAPI
import ru.snowadv.events_data_api.di.EventsDataModuleDependencies
import ru.snowadv.events_data_impl.di.EventsDataModuleComponentHolder
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.messages_data_api.di.MessagesDataModuleAPI
import ru.snowadv.messages_data_api.di.MessagesDataModuleDependencies
import ru.snowadv.messages_data_impl.di.MessagesDataModuleComponentHolder
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.dependency_holder.BaseDependencyHolder
import ru.snowadv.module_injector.dependency_holder.DependencyHolder0
import ru.snowadv.module_injector.dependency_holder.DependencyHolder1
import ru.snowadv.module_injector.dependency_holder.DependencyHolder2
import ru.snowadv.module_injector.dependency_holder.DependencyHolder6
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.di.holder.NetworkModuleAPI
import ru.snowadv.network.di.holder.NetworkModuleComponentHolder
import ru.snowadv.network.di.holder.NetworkModuleDependencies
import ru.snowadv.profile.di.holder.ProfileFeatureComponentHolder
import ru.snowadv.profile.di.holder.ProfileFeatureDependencies
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.properties_provider_api.AuthUserPropertyRepository
import ru.snowadv.properties_provider_api.di.PropertiesProviderModuleAPI
import ru.snowadv.properties_provider_api.di.PropertiesProviderModuleDependencies
import ru.snowadv.properties_provider_impl.di.PropertiesProviderModuleComponentHolder
import ru.snowadv.users_data_api.UserDataRepository
import ru.snowadv.users_data_api.di.UsersDataModuleAPI
import ru.snowadv.users_data_api.di.UsersDataModuleDependencies
import ru.snowadv.users_data_impl.di.UsersDataModuleComponentHolder
import ru.snowadv.voiceapp.di.holder.AppModuleAPI
import ru.snowadv.voiceapp.di.holder.AppModuleComponentHolder
import ru.snowadv.voiceapp.di.holder.AppModuleDependencies

/**
 * This injector injects dependencies into other modules' component holders
 */
internal object ModulesInjector {

    fun inject(appContext: Context) {
        AppModuleComponentHolder.dependencyProvider = {
            // App
            class AppModuleDependencyHolder(
                override val block: (BaseDependencyHolder<AppModuleDependencies>, AuthDataModuleAPI, ChannelsDataModuleAPI,
                                     EmojisDataModuleAPI, EventsDataModuleAPI, MessagesDataModuleAPI, UsersDataModuleAPI) -> AppModuleDependencies
            ) : DependencyHolder6<AuthDataModuleAPI, ChannelsDataModuleAPI, EmojisDataModuleAPI, EventsDataModuleAPI, MessagesDataModuleAPI, UsersDataModuleAPI, AppModuleDependencies>(
                api1 = AuthDataModuleComponentHolder.get(),
                api2 = ChannelsDataModuleComponentHolder.get(),
                api3 = EmojisDataModuleComponentHolder.get(),
                api4 = EventsDataModuleComponentHolder.get(),
                api5 = MessagesDataModuleComponentHolder.get(),
                api6 = UsersDataModuleComponentHolder.get(),
            )

            AppModuleDependencyHolder { dependencyHolder, authApi, channelsApi, emojisApi, eventsApi, messagesApi, usersApi ->
                object : AppModuleDependencies {
                    override val authDataRepository: AuthDataRepository = authApi.authDataRepo
                    override val streamDataRepository: StreamDataRepository = channelsApi.streamDataRepo
                    override val topicDataRepository: TopicDataRepository = channelsApi.topicDataRepo
                    override val emojiDataRepository: EmojiDataRepository = emojisApi.emojiDataRepo
                    override val eventRepository: EventRepository = eventsApi.eventRepo
                    override val messageDataRepository: MessageDataRepository = messagesApi.messageDataRepo
                    override val userDataRepository: UserDataRepository = usersApi.userDataRepo
                    override val appContext: Context = appContext
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }
        // Features

        ProfileFeatureComponentHolder.dependencyProvider = {
            class ProfileFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<ProfileFeatureDependencies>, AppModuleAPI, EventsDataModuleAPI) -> ProfileFeatureDependencies
            ) : DependencyHolder2<AppModuleAPI, EventsDataModuleAPI, ProfileFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = EventsDataModuleComponentHolder.get(),
            )

            ProfileFeatureDependencyHolder { dependencyHolder, appApi, eventApi ->
                object : ProfileFeatureDependencies {
                    override val router: ProfileRouter = appApi.profileRouter
                    override val repo: ProfileRepository = appApi.profileRepo
                    override val eventRepo: EventRepository = eventApi.eventRepo
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        // Data

        AuthDataModuleComponentHolder.dependencyProvider = {
            class AuthModuleDependencyHolder(
                override val block: (BaseDependencyHolder<AuthDataModuleDependencies>, PropertiesProviderModuleAPI) -> AuthDataModuleDependencies
            ): DependencyHolder1<PropertiesProviderModuleAPI, AuthDataModuleDependencies>(
                api1 = PropertiesProviderModuleComponentHolder.get(),
            )


            AuthModuleDependencyHolder { dependencyHolder, propsProviderApi ->
                object : AuthDataModuleDependencies {
                    override val authProviderPropertyRepository: AuthUserPropertyRepository = propsProviderApi.authUserPropertyRepository
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        ChannelsDataModuleComponentHolder.dependencyProvider = {
            class ChannelsDependencyHolder(
                override val block: (BaseDependencyHolder<ChannelsDataModuleDependencies>, AppModuleAPI, NetworkModuleAPI) -> ChannelsDataModuleDependencies

            ): DependencyHolder2<AppModuleAPI, NetworkModuleAPI, ChannelsDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkModuleComponentHolder.get(),
            )

            ChannelsDependencyHolder { dependencyHolder, appApi, networkApi ->
                object : ChannelsDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        EmojisDataModuleComponentHolder.dependencyProvider = {
            class EmojisDependencyHolder(
                override val block: (BaseDependencyHolder<EmojisDataModuleDependencies>, AppModuleAPI, NetworkModuleAPI) -> EmojisDataModuleDependencies

            ): DependencyHolder2<AppModuleAPI, NetworkModuleAPI, EmojisDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkModuleComponentHolder.get(),
            )


            EmojisDependencyHolder { dependencyHolder, appApi, networkApi ->
                object : EmojisDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        EventsDataModuleComponentHolder.dependencyProvider = {
            class EventsDependencyHolder(
                override val block: (BaseDependencyHolder<EventsDataModuleDependencies>, AppModuleAPI, NetworkModuleAPI) -> EventsDataModuleDependencies

            ): DependencyHolder2<AppModuleAPI, NetworkModuleAPI, EventsDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkModuleComponentHolder.get(),
            )


            EventsDependencyHolder { dependencyHolder, appApi, networkApi ->
                object : EventsDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = appApi.authProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        MessagesDataModuleComponentHolder.dependencyProvider = {
            class MessagesDependencyHolder(
                override val block: (BaseDependencyHolder<MessagesDataModuleDependencies>, AppModuleAPI, NetworkModuleAPI) -> MessagesDataModuleDependencies

            ): DependencyHolder2<AppModuleAPI, NetworkModuleAPI, MessagesDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkModuleComponentHolder.get(),
            )


            MessagesDependencyHolder { dependencyHolder, appApi, networkApi ->
                object : MessagesDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = appApi.authProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        UsersDataModuleComponentHolder.dependencyProvider = {
            class UsersDependenciesHolder(
                override val block: (BaseDependencyHolder<UsersDataModuleDependencies>, AppModuleAPI, NetworkModuleAPI) -> UsersDataModuleDependencies

            ): DependencyHolder2<AppModuleAPI, NetworkModuleAPI, UsersDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkModuleComponentHolder.get(),
            )


            UsersDependenciesHolder { dependencyHolder, appApi, networkApi ->
                object : UsersDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = appApi.authProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        // Core
        NetworkModuleComponentHolder.dependencyProvider = {
            class NetworkModuleDependencyHolder(
                override val block: (BaseDependencyHolder<NetworkModuleDependencies>, AppModuleAPI) -> NetworkModuleDependencies
            ) : DependencyHolder1<AppModuleAPI, NetworkModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
            )

            NetworkModuleDependencyHolder { dependencyHolder, appApi ->
                object : NetworkModuleDependencies {
                    override val badAuthBehavior: BadAuthBehavior = appApi.badAuthBehavior
                    override val authProvider: AuthProvider = appApi.authProvider
                    override val json: Json = appApi.json
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }
        PropertiesProviderModuleComponentHolder.dependencyProvider = {
            class PropertiesProviderDependencyHolder(
                override val block: (BaseDependencyHolder<PropertiesProviderModuleDependencies>) -> PropertiesProviderModuleDependencies
            ): DependencyHolder0<PropertiesProviderModuleDependencies>()


            PropertiesProviderDependencyHolder { dependencyHolder ->
                object : PropertiesProviderModuleDependencies {
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }


    }
}
package ru.snowadv.voiceapp.glue.injector

import android.content.Context
import coil.ImageLoader
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import ru.snowadv.auth_api.di.AuthFeatureDependencies
import ru.snowadv.auth_api.domain.navigation.AuthRouter
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.auth_data_api.AuthProvider
import ru.snowadv.auth_data_api.di.AuthDataModuleAPI
import ru.snowadv.auth_data_api.di.AuthDataModuleDependencies
import ru.snowadv.auth_data_impl.di.AuthDataModuleComponentHolder
import ru.snowadv.auth_impl.di.AuthFeatureComponentHolder
import ru.snowadv.channels_api.di.ChannelsFeatureAPI
import ru.snowadv.channels_api.di.ChannelsFeatureDependencies
import ru.snowadv.channels_api.domain.navigation.ChannelsRouter
import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.channels_data_api.di.ChannelsDataModuleAPI
import ru.snowadv.channels_data_api.di.ChannelsDataModuleDependencies
import ru.snowadv.channels_data_impl.di.ChannelsDataModuleComponentHolder
import ru.snowadv.channels_impl.di.ChannelsFeatureComponentHolder
import ru.snowadv.chat_api.di.ChatFeatureDependencies
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.chat_impl.di.ChatFeatureComponentHolder
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.database.di.holder.DatabaseLibAPI
import ru.snowadv.database.di.holder.DatabaseLibComponentHolder
import ru.snowadv.database.di.holder.DatabaseLibDependencies
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleAPI
import ru.snowadv.emojis_data_api.model.di.EmojisDataModuleDependencies
import ru.snowadv.emojis_data_impl.di.EmojisDataModuleComponentHolder
import ru.snowadv.events_api.di.EventsFeatureModuleAPI
import ru.snowadv.events_api.di.EventsFeatureModuleDependencies
import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.events_impl.di.EventsDataModuleComponentHolder
import ru.snowadv.home_api.di.HomeFeatureDependencies
import ru.snowadv.home_impl.di.HomeFeatureComponentHolder
import ru.snowadv.image_loader.di.holder.ImageLoaderLibAPI
import ru.snowadv.image_loader.di.holder.ImageLoaderLibComponentHolder
import ru.snowadv.image_loader.di.holder.ImageLoaderLibDependencies
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.messages_data_api.di.MessagesDataModuleAPI
import ru.snowadv.messages_data_api.di.MessagesDataModuleDependencies
import ru.snowadv.messages_data_impl.di.MessagesDataModuleComponentHolder
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.dependency_holder.BaseDependencyHolder
import ru.snowadv.module_injector.dependency_holder.DependencyHolder0
import ru.snowadv.module_injector.dependency_holder.DependencyHolder1
import ru.snowadv.module_injector.dependency_holder.DependencyHolder2
import ru.snowadv.module_injector.dependency_holder.DependencyHolder3
import ru.snowadv.module_injector.dependency_holder.DependencyHolder4
import ru.snowadv.module_injector.dependency_holder.DependencyHolder5
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.di.holder.NetworkLibAPI
import ru.snowadv.network.di.holder.NetworkLibComponentHolder
import ru.snowadv.network.di.holder.NetworkLibDependencies
import ru.snowadv.network_authorizer.api.ZulipAuthApi
import ru.snowadv.network_authorizer.di.holder.NetworkAuthorizerLibAPI
import ru.snowadv.network_authorizer.di.holder.NetworkAuthorizerLibComponentHolder
import ru.snowadv.network_authorizer.di.holder.NetworkAuthorizerLibDependencies
import ru.snowadv.people_api.di.PeopleFeatureAPI
import ru.snowadv.people_api.di.PeopleFeatureDependencies
import ru.snowadv.people_api.domain.navigation.PeopleRouter
import ru.snowadv.people_api.presentation.PeopleScreenFactory
import ru.snowadv.people_impl.di.PeopleFeatureComponentHolder
import ru.snowadv.profile_api.di.ProfileFeatureAPI
import ru.snowadv.profile_api.di.ProfileFeatureDependencies
import ru.snowadv.profile_api.domain.navigation.ProfileRouter
import ru.snowadv.profile_api.presentation.ProfileScreenFactory
import ru.snowadv.profile_impl.di.ProfileFeatureComponentHolder
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
                override val block: (BaseDependencyHolder<AppModuleDependencies>) -> AppModuleDependencies
            ) : DependencyHolder0<AppModuleDependencies>()

            AppModuleDependencyHolder { dependencyHolder ->
                object : AppModuleDependencies {
                    override val appContext: Context = appContext
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }
        // Features

        AuthFeatureComponentHolder.dependencyProvider = {
            class AuthFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<AuthFeatureDependencies>, AppModuleAPI, AuthDataModuleAPI) -> AuthFeatureDependencies
            ) : DependencyHolder2<AppModuleAPI, AuthDataModuleAPI, AuthFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = AuthDataModuleComponentHolder.get(),
            )

            AuthFeatureDependencyHolder { dependencyHolder, appApi, authDataApi ->
                object : AuthFeatureDependencies {
                    override val router: AuthRouter = appApi.authRouter
                    override val authDataRepo: AuthDataRepository = authDataApi.authDataRepo
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ChannelsFeatureComponentHolder.dependencyProvider = {
            class ChannelsFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<ChannelsFeatureDependencies>, AppModuleAPI, EventsFeatureModuleAPI, ChannelsDataModuleAPI) -> ChannelsFeatureDependencies
            ) : DependencyHolder3<AppModuleAPI, EventsFeatureModuleAPI, ChannelsDataModuleAPI, ChannelsFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = EventsDataModuleComponentHolder.get(),
                api3 = ChannelsDataModuleComponentHolder.get(),
            )

            ChannelsFeatureDependencyHolder { dependencyHolder, appApi, eventApi, channelsData ->
                object : ChannelsFeatureDependencies {
                    override val router: ChannelsRouter = appApi.channelsRouter
                    override val streamDataRepo: StreamDataRepository = channelsData.streamDataRepo
                    override val topicDataRepo: TopicDataRepository = channelsData.topicDataRepo
                    override val eventRepo: EventRepository = eventApi.eventRepo
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder
                }
            }.dependencies
        }

        ChatFeatureComponentHolder.dependencyProvider = {
            class ChatFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<ChatFeatureDependencies>, AppModuleAPI, EmojisDataModuleAPI, MessagesDataModuleAPI, EventsFeatureModuleAPI, ImageLoaderLibAPI) -> ChatFeatureDependencies
            ) : DependencyHolder5<AppModuleAPI, EmojisDataModuleAPI, MessagesDataModuleAPI, EventsFeatureModuleAPI, ImageLoaderLibAPI, ChatFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = EmojisDataModuleComponentHolder.get(),
                api3 = MessagesDataModuleComponentHolder.get(),
                api4 = EventsDataModuleComponentHolder.get(),
                api5 = ImageLoaderLibComponentHolder.get(),
            )

            ChatFeatureDependencyHolder { dependencyHolder, appApi, emojisApi, msgsApi, eventApi, imageLoaderApi ->
                object : ChatFeatureDependencies {
                    override val router: ChatRouter = appApi.chatRouter
                    override val emojiDataRepo: EmojiDataRepository = emojisApi.emojiDataRepo
                    override val messageDataRepo: MessageDataRepository = msgsApi.messageDataRepo
                    override val eventRepository: EventRepository = eventApi.eventRepo
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val appContext: Context = appContext
                    override val coilImageLoader: ImageLoader = imageLoaderApi.coilImageLoader
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        HomeFeatureComponentHolder.dependencyProvider = {
            class ChatFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<HomeFeatureDependencies>, AppModuleAPI, ChannelsFeatureAPI, PeopleFeatureAPI, ProfileFeatureAPI) -> HomeFeatureDependencies
            ) : DependencyHolder4<AppModuleAPI, ChannelsFeatureAPI, PeopleFeatureAPI, ProfileFeatureAPI, HomeFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = ChannelsFeatureComponentHolder.get(),
                api3 = PeopleFeatureComponentHolder.get(),
                api4 = ProfileFeatureComponentHolder.get(),
            )

            ChatFeatureDependencyHolder { dependencyHolder, appApi, channelsFeature, peopleFeature, profileFeature ->
                object : HomeFeatureDependencies {
                    override val channelsScreenFactory: ChannelsScreenFactory = channelsFeature.screenFactory
                    override val peopleScreenFactory: PeopleScreenFactory = peopleFeature.screenFactory
                    override val profileScreenFactory: ProfileScreenFactory = profileFeature.screenFactory
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        PeopleFeatureComponentHolder.dependencyProvider = {
            class PeopleFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<PeopleFeatureDependencies>, AppModuleAPI, EventsFeatureModuleAPI, UsersDataModuleAPI) -> PeopleFeatureDependencies
            ) : DependencyHolder3<AppModuleAPI, EventsFeatureModuleAPI, UsersDataModuleAPI, PeopleFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = EventsDataModuleComponentHolder.get(),
                api3 = UsersDataModuleComponentHolder.get(),
            )

            PeopleFeatureDependencyHolder { dependencyHolder, appApi, eventApi, usersApi ->
                object : PeopleFeatureDependencies {
                    override val router: PeopleRouter = appApi.peopleRouter
                    override val userDataRepo: UserDataRepository = usersApi.userDataRepo
                    override val eventRepo: EventRepository = eventApi.eventRepo
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ProfileFeatureComponentHolder.dependencyProvider = {
            class ProfileFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<ProfileFeatureDependencies>, AppModuleAPI, EventsFeatureModuleAPI, UsersDataModuleAPI) -> ProfileFeatureDependencies
            ) : DependencyHolder3<AppModuleAPI, EventsFeatureModuleAPI, UsersDataModuleAPI, ProfileFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = EventsDataModuleComponentHolder.get(),
                api3 = UsersDataModuleComponentHolder.get(),
            )

            ProfileFeatureDependencyHolder { dependencyHolder, appApi, eventApi, usersApi ->
                object : ProfileFeatureDependencies {
                    override val router: ProfileRouter = appApi.profileRouter
                    override val userDataRepo: UserDataRepository = usersApi.userDataRepo
                    override val eventRepo: EventRepository = eventApi.eventRepo
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder
                }
            }.dependencies
        }

        // Data

        AuthDataModuleComponentHolder.dependencyProvider = {
            class AuthModuleDependencyHolder(
                override val block: (BaseDependencyHolder<AuthDataModuleDependencies>, NetworkAuthorizerLibAPI, DatabaseLibAPI) -> AuthDataModuleDependencies
            ) : DependencyHolder2<NetworkAuthorizerLibAPI, DatabaseLibAPI, AuthDataModuleDependencies>(
                api1 = NetworkAuthorizerLibComponentHolder.get(),
                api2 = DatabaseLibComponentHolder.get(),
            )


            AuthModuleDependencyHolder { dependencyHolder, networkApi, dbApi ->
                object : AuthDataModuleDependencies {
                    override val authApi: ZulipAuthApi = networkApi.zulipAuthApi
                    override val authDao: AuthDao = dbApi.authDao
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ChannelsDataModuleComponentHolder.dependencyProvider = {
            class ChannelsDependencyHolder(
                override val block: (BaseDependencyHolder<ChannelsDataModuleDependencies>, AppModuleAPI, NetworkLibAPI, DatabaseLibAPI) -> ChannelsDataModuleDependencies

            ) : DependencyHolder3<AppModuleAPI, NetworkLibAPI, DatabaseLibAPI, ChannelsDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = DatabaseLibComponentHolder.get(),
            )

            ChannelsDependencyHolder { dependencyHolder, appApi, networkApi, dbApi ->
                object : ChannelsDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val streamsDao: StreamsDao = dbApi.streamsDao
                    override val topicsDao: TopicsDao = dbApi.topicsDao
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        EmojisDataModuleComponentHolder.dependencyProvider = {
            class EmojisDependencyHolder(
                override val block: (BaseDependencyHolder<EmojisDataModuleDependencies>, AppModuleAPI, NetworkLibAPI, DatabaseLibAPI) -> EmojisDataModuleDependencies

            ) : DependencyHolder3<AppModuleAPI, NetworkLibAPI, DatabaseLibAPI, EmojisDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = DatabaseLibComponentHolder.get(),
            )


            EmojisDependencyHolder { dependencyHolder, appApi, networkApi, dbApi ->
                object : EmojisDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val emojisDao: EmojisDao = dbApi.emojisDao
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        EventsDataModuleComponentHolder.dependencyProvider = {
            class EventsDependencyHolder(
                override val block: (BaseDependencyHolder<EventsFeatureModuleDependencies>, AppModuleAPI, NetworkLibAPI, AuthDataModuleAPI) -> EventsFeatureModuleDependencies

            ) : DependencyHolder3<AppModuleAPI, NetworkLibAPI, AuthDataModuleAPI, EventsFeatureModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = AuthDataModuleComponentHolder.get(),
            )


            EventsDependencyHolder { dependencyHolder, appApi, networkApi, authDataApi ->
                object : EventsFeatureModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = authDataApi.authProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        MessagesDataModuleComponentHolder.dependencyProvider = {
            class MessagesDependencyHolder(
                override val block: (BaseDependencyHolder<MessagesDataModuleDependencies>, AppModuleAPI, NetworkLibAPI, AuthDataModuleAPI, DatabaseLibAPI) -> MessagesDataModuleDependencies

            ) : DependencyHolder4<AppModuleAPI, NetworkLibAPI, AuthDataModuleAPI, DatabaseLibAPI, MessagesDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = AuthDataModuleComponentHolder.get(),
                api4 = DatabaseLibComponentHolder.get(),
            )


            MessagesDependencyHolder { dependencyHolder, appApi, networkApi, authDataApi, dbApi ->
                object : MessagesDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = authDataApi.authProvider
                    override val messagesDao: MessagesDao = dbApi.messagesDao
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        UsersDataModuleComponentHolder.dependencyProvider = {
            class UsersDependenciesHolder(
                override val block: (BaseDependencyHolder<UsersDataModuleDependencies>, AppModuleAPI, NetworkLibAPI, AuthDataModuleAPI, DatabaseLibAPI) -> UsersDataModuleDependencies

            ) : DependencyHolder4<AppModuleAPI, NetworkLibAPI, AuthDataModuleAPI, DatabaseLibAPI, UsersDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = AuthDataModuleComponentHolder.get(),
                api4 = DatabaseLibComponentHolder.get(),
            )


            UsersDependenciesHolder { dependencyHolder, appApi, networkApi, authDataApi, dbApi ->
                object : UsersDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = authDataApi.authProvider
                    override val usersDao: UsersDao = dbApi.usersDao
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        // Lib
        NetworkLibComponentHolder.dependencyProvider = {
            class NetworkLibDependencyHolder(
                override val block: (BaseDependencyHolder<NetworkLibDependencies>, AppModuleAPI, AuthDataModuleAPI) -> NetworkLibDependencies
            ) : DependencyHolder2<AppModuleAPI, AuthDataModuleAPI, NetworkLibDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = AuthDataModuleComponentHolder.get(),
            )

            NetworkLibDependencyHolder { dependencyHolder, appApi, authDataApi ->
                object : NetworkLibDependencies {
                    override val badAuthBehavior: BadAuthBehavior = appApi.badAuthBehavior
                    override val authProvider: AuthProvider = authDataApi.authProvider
                    override val json: Json = appApi.json
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        NetworkAuthorizerLibComponentHolder.dependencyProvider = {
            class NetworkAuthorizerLibDependencyHolder(
                override val block: (BaseDependencyHolder<NetworkAuthorizerLibDependencies>, AppModuleAPI) -> NetworkAuthorizerLibDependencies
            ) : DependencyHolder1<AppModuleAPI, NetworkAuthorizerLibDependencies>(
                api1 = AppModuleComponentHolder.get(),
            )

            NetworkAuthorizerLibDependencyHolder { dependencyHolder, appApi ->
                object : NetworkAuthorizerLibDependencies {
                    override val json: Json = appApi.json
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        DatabaseLibComponentHolder.dependencyProvider = {
            class DatabaseLibDependencyHolder(
                override val block: (BaseDependencyHolder<DatabaseLibDependencies>, AppModuleAPI) -> DatabaseLibDependencies
            ) : DependencyHolder1<AppModuleAPI, DatabaseLibDependencies>(
                api1 = AppModuleComponentHolder.get(),
            )

            DatabaseLibDependencyHolder { dependencyHolder, appApi ->
                object : DatabaseLibDependencies {
                    override val appContext: Context = appContext
                    override val json: Json = appApi.json
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }

        ImageLoaderLibComponentHolder.dependencyProvider = {
            class ImageLoaderLibDependencyHolder(
                override val block: (BaseDependencyHolder<ImageLoaderLibDependencies>, NetworkLibAPI) -> ImageLoaderLibDependencies
            ) : DependencyHolder1<NetworkLibAPI, ImageLoaderLibDependencies>(
                api1 = NetworkLibComponentHolder.get(),
            )

            ImageLoaderLibDependencyHolder { dependencyHolder, networkApi ->
                object : ImageLoaderLibDependencies {
                    override val okHttpClient: OkHttpClient = networkApi.okHttpClient
                    override val appContext: Context = appContext
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }


    }
}
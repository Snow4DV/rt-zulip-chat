package ru.snowadv.chatapp.glue.injector

import android.content.Context
import coil.ImageLoader
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import ru.snowadv.auth_data.di.holder.AuthDataAPI
import ru.snowadv.auth_data.di.holder.AuthDataComponentHolder
import ru.snowadv.auth_data.di.holder.AuthDataDependencies
import ru.snowadv.auth_data.di.holder.ChatDataAPI
import ru.snowadv.auth_data.di.holder.ChatDataComponentHolder
import ru.snowadv.auth_data.di.holder.ChatDataDependencies
import ru.snowadv.auth_domain_api.di.AuthDomainAPI
import ru.snowadv.auth_domain_api.di.AuthDomainDependencies
import ru.snowadv.auth_domain_api.repository.AuthRepository
import ru.snowadv.auth_domain_api.use_case.AuthorizeUseCase
import ru.snowadv.auth_domain_api.use_case.ClearAuthUseCase
import ru.snowadv.auth_domain_api.use_case.ValidateEmailUseCase
import ru.snowadv.auth_domain_api.use_case.ValidatePasswordUseCase
import ru.snowadv.auth_domain_impl.di.AuthDomainComponentHolder
import ru.snowadv.auth_presentation.di.holder.AuthPresentationComponentHolder
import ru.snowadv.auth_presentation.di.holder.AuthPresentationDependencies
import ru.snowadv.auth_presentation.navigation.AuthRouter
import ru.snowadv.auth_storage.di.holder.AuthStorageAPI
import ru.snowadv.auth_storage.di.holder.AuthStorageComponentHolder
import ru.snowadv.auth_storage.di.holder.AuthStorageDependencies
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.channels_data.di.holder.ChannelsDataAPI
import ru.snowadv.channels_data.di.holder.ChannelsDataComponentHolder
import ru.snowadv.channels_data.di.holder.ChannelsDataDependencies
import ru.snowadv.channels_domain_api.di.ChannelsDomainAPI
import ru.snowadv.channels_domain_api.di.ChannelsDomainDependencies
import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.channels_domain_api.repository.TopicRepository
import ru.snowadv.channels_domain_api.use_case.GetStreamsUseCase
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_api.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels_domain_impl.di.ChannelsDomainComponentHolder
import ru.snowadv.channels_presentation.navigation.ChannelsRouter
import ru.snowadv.channels_presentation.api.ChannelsScreenFactory
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationAPI
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationComponentHolder
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationDependencies
import ru.snowadv.chat_domain_api.di.ChatDomainAPI
import ru.snowadv.chat_domain_api.di.ChatDomainDependencies
import ru.snowadv.chat_domain_api.repository.EmojiRepository
import ru.snowadv.chat_domain_api.repository.MessageRepository
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_domain_impl.di.ChatDomainComponentHolder
import ru.snowadv.chat_presentation.di.holder.ChatPresentationComponentHolder
import ru.snowadv.chat_presentation.di.holder.ChatPresentationDependencies
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.database.di.holder.DatabaseLibAPI
import ru.snowadv.database.di.holder.DatabaseLibComponentHolder
import ru.snowadv.database.di.holder.DatabaseLibDependencies
import ru.snowadv.events_api.repository.EventRepository
import ru.snowadv.events_impl.di.dagger.EventsDataModuleComponentHolder
import ru.snowadv.events_impl.di.holder.EventsDataAPI
import ru.snowadv.events_impl.di.holder.EventsDataDependencies
import ru.snowadv.image_loader.di.holder.ImageLoaderLibAPI
import ru.snowadv.image_loader.di.holder.ImageLoaderLibComponentHolder
import ru.snowadv.image_loader.di.holder.ImageLoaderLibDependencies
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.dependency_holder.BaseDependencyHolder
import ru.snowadv.module_injector.dependency_holder.DependencyHolder0
import ru.snowadv.module_injector.dependency_holder.DependencyHolder1
import ru.snowadv.module_injector.dependency_holder.DependencyHolder2
import ru.snowadv.module_injector.dependency_holder.DependencyHolder3
import ru.snowadv.module_injector.dependency_holder.DependencyHolder4
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
import ru.snowadv.chatapp.di.holder.AppModuleAPI
import ru.snowadv.chatapp.di.holder.AppModuleComponentHolder
import ru.snowadv.chatapp.di.holder.AppModuleDependencies
import ru.snowadv.home_presentation.di.dagger.HomePresentationComponentHolder
import ru.snowadv.home_presentation.di.holder.HomePresentationDependencies
import ru.snowadv.network.api.LoggerToggle
import ru.snowadv.people_presentation.api.PeopleScreenFactory
import ru.snowadv.people_presentation.di.holder.PeoplePresentationAPI
import ru.snowadv.people_presentation.di.holder.PeoplePresentationComponentHolder
import ru.snowadv.people_presentation.di.holder.PeoplePresentationDependencies
import ru.snowadv.people_presentation.navigation.PeopleRouter
import ru.snowadv.profile_presentation.api.ProfileScreenFactory
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationAPI
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationComponentHolder
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationDependencies
import ru.snowadv.profile_presentation.navigation.ProfileRouter
import ru.snowadv.users_data.di.holder.UsersDataAPI
import ru.snowadv.users_data.di.holder.UsersDataComponentHolder
import ru.snowadv.users_data.di.holder.UsersDataDependencies
import ru.snowadv.users_domain_api.di.UsersDomainAPI
import ru.snowadv.users_domain_api.di.UsersDomainDependencies
import ru.snowadv.users_domain_api.repository.UserRepository
import ru.snowadv.users_domain_api.use_case.GetPeopleUseCase
import ru.snowadv.users_domain_api.use_case.GetProfileUseCase
import ru.snowadv.users_domain_api.use_case.ListenToPeoplePresenceEventsUseCase
import ru.snowadv.users_domain_api.use_case.ListenToProfilePresenceEventsUseCase
import ru.snowadv.users_domain_impl.di.UsersDomainComponentHolder

/**
 * This injector injects dependencies into other modules' component holders
 */
abstract class BaseModulesInjector {

    open fun inject(appContext: Context) {
        // App
        AppModuleComponentHolder.dependencyProvider = {
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

        // Auth
        AuthDataComponentHolder.dependencyProvider = {
            class AuthDataDependencyHolder(
                override val block: (BaseDependencyHolder<AuthDataDependencies>, AuthStorageAPI) -> AuthDataDependencies
            ) : DependencyHolder1<AuthStorageAPI, AuthDataDependencies>(
                api1 = AuthStorageComponentHolder.get(),
            )


            AuthDataDependencyHolder { dependencyHolder, authStorageApi ->
                object : AuthDataDependencies {
                    override val authStorageRepository: AuthStorageRepository = authStorageApi.authStorageRepository
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        AuthDomainComponentHolder.dependencyProvider = {
            class AuthDomainDependencyHolder(
                override val block: (BaseDependencyHolder<AuthDomainDependencies>, AuthDataAPI) -> AuthDomainDependencies
            ) : DependencyHolder1<AuthDataAPI, AuthDomainDependencies>(
                api1 = AuthDataComponentHolder.get(),
            )

            AuthDomainDependencyHolder { dependencyHolder, authDataApi ->
                object : AuthDomainDependencies {
                    override val repo: AuthRepository = authDataApi.authRepo
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        AuthPresentationComponentHolder.dependencyProvider = {
            class AuthPresentationDependencyHolder(
                override val block: (BaseDependencyHolder<AuthPresentationDependencies>, AppModuleAPI, AuthDomainAPI) -> AuthPresentationDependencies
            ) : DependencyHolder2<AppModuleAPI, AuthDomainAPI, AuthPresentationDependencies>(
                api1 = AppModuleComponentHolder.get() ,
                api2 = AuthDomainComponentHolder.get(),
            )

            AuthPresentationDependencyHolder { dependencyHolder, appApi, authApi ->
                object : AuthPresentationDependencies {
                    override val authUseCase: AuthorizeUseCase = authApi.authUseCase
                    override val clearAuthUseCase: ClearAuthUseCase = authApi.clearAuthUseCase
                    override val validateEmailUseCase: ValidateEmailUseCase = authApi.validateEmailUseCase
                    override val validatePasswordUseCase: ValidatePasswordUseCase = authApi.validatePasswordUseCase
                    override val authRouter: AuthRouter = appApi.authRouter
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        // Chat
        ChatDataComponentHolder.dependencyProvider = {
            class ChatDataDependencyHolder(
                override val block: (BaseDependencyHolder<ChatDataDependencies>, NetworkLibAPI, DatabaseLibAPI, AuthStorageAPI, AppModuleAPI) -> ChatDataDependencies
            ) : DependencyHolder4<NetworkLibAPI, DatabaseLibAPI, AuthStorageAPI, AppModuleAPI, ChatDataDependencies>(
                api1 = NetworkLibComponentHolder.get(),
                api2 = DatabaseLibComponentHolder.get(),
                api3 = AuthStorageComponentHolder.get(),
                api4 = AppModuleComponentHolder.get(),
            )


            ChatDataDependencyHolder { dependencyHolder, networkApi,  dbApi, authApi, appApi ->
                object : ChatDataDependencies {
                    override val zulipApi: ZulipApi = networkApi.zulipApi
                    override val emojisDao: EmojisDao = dbApi.emojisDao
                    override val messagesDao: MessagesDao = dbApi.messagesDao
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val authProvider: AuthProvider = authApi.authProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ChatDomainComponentHolder.dependencyProvider = {
            class ChatDomainDependencyHolder(
                override val block: (BaseDependencyHolder<ChatDomainDependencies>, ChatDataAPI, EventsDataAPI) -> ChatDomainDependencies
            ) : DependencyHolder2<ChatDataAPI, EventsDataAPI, ChatDomainDependencies>(
                api1 = ChatDataComponentHolder.get(),
                api2 = EventsDataModuleComponentHolder.get(),
            )

            ChatDomainDependencyHolder { dependencyHolder, chatDataApi, eventsDataApi ->
                object : ChatDomainDependencies {
                    override val emojiRepository: EmojiRepository = chatDataApi.emojiRepo
                    override val messageRepository: MessageRepository = chatDataApi.messageRepo
                    override val eventRepository: EventRepository = eventsDataApi.eventRepo
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ChatPresentationComponentHolder.dependencyProvider = {
            class ChatPresentationDependencyHolder(
                override val block: (BaseDependencyHolder<ChatPresentationDependencies>, AppModuleAPI, ChatDomainAPI, ImageLoaderLibAPI) -> ChatPresentationDependencies
            ) : DependencyHolder3<AppModuleAPI, ChatDomainAPI, ImageLoaderLibAPI, ChatPresentationDependencies>(
                api1 = AppModuleComponentHolder.get() ,
                api2 = ChatDomainComponentHolder.get(),
                api3 = ImageLoaderLibComponentHolder.get(),
            )

            ChatPresentationDependencyHolder { dependencyHolder, appApi, chatApi, imageLoaderApi ->
                object : ChatPresentationDependencies {
                    override val chatRouter: ChatRouter = appApi.chatRouter
                    override val addReactionUseCase: AddReactionUseCase = chatApi.addReactionUseCase
                    override val removeReactionUseCase: RemoveReactionUseCase = chatApi.removeReactionUseCase
                    override val sendMessageUseCase: SendMessageUseCase = chatApi.sendMessageUseCase
                    override val getMessagesUseCase: GetCurrentMessagesUseCase = chatApi.getCurrentMessagesUseCase
                    override val listenToChatEventsUseCase: ListenToChatEventsUseCase = chatApi.listenToChatEventsUseCase
                    override val loadMoreMessagesUseCase: LoadMoreMessagesUseCase = chatApi.loadMoreMessagesUseCase
                    override val sendFileUseCase: SendFileUseCase = chatApi.sendFileUseCase
                    override val getEmojisUseCase: GetEmojisUseCase = chatApi.getEmojisUseCase
                    override val appContext: Context = appContext
                    override val imageLoader: ImageLoader = imageLoaderApi.coilImageLoader
                    override val baseUrlProvider: BaseUrlProvider = appApi.baseUrlProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        // Events
        EventsDataModuleComponentHolder.dependencyProvider = {
            class EventsDependencyHolder(
                override val block: (BaseDependencyHolder<EventsDataDependencies>, AppModuleAPI, NetworkLibAPI, AuthStorageAPI) -> EventsDataDependencies

            ) : DependencyHolder3<AppModuleAPI, NetworkLibAPI, AuthStorageAPI, EventsDataDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = AuthStorageComponentHolder.get(),
            )


            EventsDependencyHolder { dependencyHolder, appApi, networkApi, authStorageApi ->
                object : EventsDataDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = authStorageApi.authProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        // Channels

        ChannelsDataComponentHolder.dependencyProvider = {
            class ChannelsDataDependencyHolder(
                override val block: (BaseDependencyHolder<ChannelsDataDependencies>, NetworkLibAPI, DatabaseLibAPI, AppModuleAPI) -> ChannelsDataDependencies
            ) : DependencyHolder3<NetworkLibAPI, DatabaseLibAPI, AppModuleAPI, ChannelsDataDependencies>(
                api1 = NetworkLibComponentHolder.get(),
                api2 = DatabaseLibComponentHolder.get(),
                api3 = AppModuleComponentHolder.get(),
            )


            ChannelsDataDependencyHolder { dependencyHolder, networkApi,  dbApi, appApi ->
                object : ChannelsDataDependencies {
                    override val zulipApi: ZulipApi = networkApi.zulipApi
                    override val streamsDao: StreamsDao = dbApi.streamsDao
                    override val topicsDao: TopicsDao = dbApi.topicsDao
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ChannelsDomainComponentHolder.dependencyProvider = {
            class ChannelsDomainDependencyHolder(
                override val block: (BaseDependencyHolder<ChannelsDomainDependencies>, ChannelsDataAPI, EventsDataAPI) -> ChannelsDomainDependencies
            ) : DependencyHolder2<ChannelsDataAPI, EventsDataAPI, ChannelsDomainDependencies>(
                api1 = ChannelsDataComponentHolder.get(),
                api2 = EventsDataModuleComponentHolder.get(),
            )

            ChannelsDomainDependencyHolder { dependencyHolder, channelsDataAPi, eventsDataApi ->
                object : ChannelsDomainDependencies {
                    override val streamRepository: StreamRepository = channelsDataAPi.streamRepo
                    override val topicRepository: TopicRepository = channelsDataAPi.topicRepo
                    override val eventRepository: EventRepository = eventsDataApi.eventRepo
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ChannelsPresentationComponentHolder.dependencyProvider = {
            class ChannelsPresentationDependencyHolder(
                override val block: (BaseDependencyHolder<ChannelsPresentationDependencies>, AppModuleAPI, ChannelsDomainAPI) -> ChannelsPresentationDependencies
            ) : DependencyHolder2<AppModuleAPI, ChannelsDomainAPI, ChannelsPresentationDependencies>(
                api1 = AppModuleComponentHolder.get() ,
                api2 = ChannelsDomainComponentHolder.get(),
            )

            ChannelsPresentationDependencyHolder { dependencyHolder, appApi, channelsDomainAPi ->
                object : ChannelsPresentationDependencies {
                    override val channelsRouter: ChannelsRouter = appApi.channelsRouter
                    override val getStreamsUseCase: GetStreamsUseCase = channelsDomainAPi.getStreamsUseCase
                    override val getTopicsUseCase: GetTopicsUseCase = channelsDomainAPi.getTopicsUseCase
                    override val listenToStreamEventsUseCase: ListenToStreamEventsUseCase = channelsDomainAPi.listenToStreamEventsUseCase
                    override val appContext: Context = appContext
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        // Users

        UsersDataComponentHolder.dependencyProvider = {
            class UsersDataDependencyHolder(
                override val block: (BaseDependencyHolder<UsersDataDependencies>, NetworkLibAPI, DatabaseLibAPI, AppModuleAPI, AuthStorageAPI) -> UsersDataDependencies
            ) : DependencyHolder4<NetworkLibAPI, DatabaseLibAPI, AppModuleAPI, AuthStorageAPI, UsersDataDependencies>(
                api1 = NetworkLibComponentHolder.get(),
                api2 = DatabaseLibComponentHolder.get(),
                api3 = AppModuleComponentHolder.get(),
                api4 = AuthStorageComponentHolder.get(),
            )


            UsersDataDependencyHolder { dependencyHolder, networkApi,  dbApi, appApi, authStorageApi ->
                object : UsersDataDependencies {
                    override val zulipApi: ZulipApi = networkApi.zulipApi
                    override val usersDao: UsersDao = dbApi.usersDao
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val authProvider: AuthProvider = authStorageApi.authProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        UsersDomainComponentHolder.dependencyProvider = {
            class UsersDomainDependencyHolder(
                override val block: (BaseDependencyHolder<UsersDomainDependencies>, UsersDataAPI, EventsDataAPI, AppModuleAPI) -> UsersDomainDependencies
            ) : DependencyHolder3<UsersDataAPI, EventsDataAPI, AppModuleAPI, UsersDomainDependencies>(
                api1 = UsersDataComponentHolder.get(),
                api2 = EventsDataModuleComponentHolder.get(),
                api3 = AppModuleComponentHolder.get(),
            )

            UsersDomainDependencyHolder { dependencyHolder, usersDataApi, eventsDataApi, appApi ->
                object : UsersDomainDependencies {
                    override val profileRepository: UserRepository = usersDataApi.userRepo
                    override val eventRepository: EventRepository = eventsDataApi.eventRepo
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        // People

        PeoplePresentationComponentHolder.dependencyProvider = {
            class PeoplePresentationDependencyHolder(
                override val block: (BaseDependencyHolder<PeoplePresentationDependencies>, AppModuleAPI, UsersDomainAPI) -> PeoplePresentationDependencies
            ) : DependencyHolder2<AppModuleAPI, UsersDomainAPI, PeoplePresentationDependencies>(
                api1 = AppModuleComponentHolder.get() ,
                api2 = UsersDomainComponentHolder.get(),
            )

            PeoplePresentationDependencyHolder { dependencyHolder, appApi, usersDomainApi ->
                object : PeoplePresentationDependencies {
                    override val peopleRouter: PeopleRouter = appApi.peopleRouter
                    override val getPeopleUseCase: GetPeopleUseCase = usersDomainApi.getPeopleUseCase
                    override val listenToPeopleRouter: ListenToPeoplePresenceEventsUseCase = usersDomainApi.listenToPeoplePresenceEventsUseCase
                    override val appContext: Context = appContext
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        // Profile

        ProfilePresentationComponentHolder.dependencyProvider = {
            class ProfilePresentationDependencyHolder(
                override val block: (BaseDependencyHolder<ProfilePresentationDependencies>, AppModuleAPI, UsersDomainAPI) -> ProfilePresentationDependencies
            ) : DependencyHolder2<AppModuleAPI, UsersDomainAPI, ProfilePresentationDependencies>(
                api1 = AppModuleComponentHolder.get() ,
                api2 = UsersDomainComponentHolder.get(),
            )

            ProfilePresentationDependencyHolder { dependencyHolder, appApi, usersDomainApi ->
                object : ProfilePresentationDependencies {
                    override val profileRouter: ProfileRouter = appApi.profileRouter
                    override val getProfileUseCase: GetProfileUseCase = usersDomainApi.getProfileUseCase
                    override val listenToProfileEventsUseCase: ListenToProfilePresenceEventsUseCase = usersDomainApi.listenToProfilePresenceEventsUseCase
                    override val appContext: Context = appContext
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        // Home
        HomePresentationComponentHolder.dependencyProvider = {
            class HomePresentationDependencyHolder(
                override val block: (BaseDependencyHolder<HomePresentationDependencies>, ChannelsPresentationAPI, PeoplePresentationAPI, ProfilePresentationAPI) -> HomePresentationDependencies
            ) : DependencyHolder3<ChannelsPresentationAPI, PeoplePresentationAPI, ProfilePresentationAPI, HomePresentationDependencies>(
                api1 = ChannelsPresentationComponentHolder.get(),
                api2 = PeoplePresentationComponentHolder.get(),
                api3 = ProfilePresentationComponentHolder.get(),
            )

            HomePresentationDependencyHolder { dependencyHolder, channelsApi, peopleApi, profileApi ->
                object : HomePresentationDependencies {
                    override val channelsScreenFactory: ChannelsScreenFactory = channelsApi.screenFactory
                    override val peopleScreenFactory: PeopleScreenFactory = peopleApi.screenFactory
                    override val profileScreenFactory: ProfileScreenFactory = profileApi.screenFactory
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }



        // Lib
        NetworkLibComponentHolder.dependencyProvider = {
            class NetworkLibDependencyHolder(
                override val block: (BaseDependencyHolder<NetworkLibDependencies>, AppModuleAPI, AuthStorageAPI) -> NetworkLibDependencies
            ) : DependencyHolder2<AppModuleAPI, AuthStorageAPI, NetworkLibDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = AuthStorageComponentHolder.get(),
            )

            NetworkLibDependencyHolder { dependencyHolder, appApi, authDataApi ->
                object : NetworkLibDependencies {
                    override val badAuthBehavior: BadAuthBehavior = appApi.badAuthBehavior
                    override val authProvider: AuthProvider = authDataApi.authProvider
                    override val json: Json = appApi.json
                    override val baseUrlProvider: BaseUrlProvider = appApi.baseUrlProvider
                    override val loggerToggle: LoggerToggle = appApi.networkLoggerToggle
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

        AuthStorageComponentHolder.dependencyProvider = {
            class AuthStorageLibDependencyHolder(
                override val block: (BaseDependencyHolder<AuthStorageDependencies>, NetworkAuthorizerLibAPI, DatabaseLibAPI) -> AuthStorageDependencies
            ) : DependencyHolder2<NetworkAuthorizerLibAPI, DatabaseLibAPI, AuthStorageDependencies>(
                api1 = NetworkAuthorizerLibComponentHolder.get(),
                api2 = DatabaseLibComponentHolder.get(),
            )

            AuthStorageLibDependencyHolder { dependencyHolder, authApi, dbApi ->
                object : AuthStorageDependencies {
                    override val zulipAuthApi: ZulipAuthApi = authApi.zulipAuthApi
                    override val authDao: AuthDao = dbApi.authDao
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }


    }
}
package ru.snowadv.chatapp.di.injector

import android.content.Context
import coil.ImageLoader
import kotlinx.serialization.json.Json
import ru.snowadv.auth_data.di.holder.AuthDataComponentHolder
import ru.snowadv.auth_data.di.holder.AuthDataDependencies
import ru.snowadv.auth_data.di.holder.ChatDataComponentHolder
import ru.snowadv.auth_data.di.holder.ChatDataDependencies
import ru.snowadv.auth_storage.di.holder.AuthStorageComponentHolder
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.channels_data.di.holder.ChannelsDataComponentHolder
import ru.snowadv.channels_data.di.holder.ChannelsDataDependencies
import ru.snowadv.channels_domain_api.di.ChannelsDomainAPI
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.channels_domain_impl.di.ChannelsDomainComponentHolder
import ru.snowadv.chat_domain_api.di.ChatDomainAPI
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_domain_impl.di.ChatDomainComponentHolder
import ru.snowadv.chat_presentation.di.holder.ChatPresentationComponentHolder
import ru.snowadv.chat_presentation.di.holder.ChatPresentationDependencies
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.image_loader.di.holder.ImageLoaderLibAPI
import ru.snowadv.image_loader.di.holder.ImageLoaderLibComponentHolder
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.module_injector.dependency_holder.BaseDependencyHolder
import ru.snowadv.module_injector.dependency_holder.DependencyHolder2
import ru.snowadv.module_injector.dependency_holder.DependencyHolder3
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.network.di.holder.NetworkLibComponentHolder
import ru.snowadv.network.di.holder.NetworkLibDependencies
import ru.snowadv.chatapp.di.holder.AppModuleAPI
import ru.snowadv.chatapp.di.holder.AppModuleComponentHolder
import ru.snowadv.chatapp.di.holder.TestAppModuleAPI
import ru.snowadv.chatapp.di.holder.TestAppModuleComponentHolder
import ru.snowadv.chatapp.di.holder.TestAppModuleDependencies
import ru.snowadv.chatapp.glue.injector.BaseModulesInjector
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.events_impl.di.dagger.EventsDataModuleComponentHolder
import ru.snowadv.events_impl.di.holder.EventsDataDependencies
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.dependency_holder.DependencyHolder1
import ru.snowadv.module_injector.dependency_holder.DependencyHolder4
import ru.snowadv.model.LoggerToggle
import ru.snowadv.module_injector.dependency_holder.DependencyHolder5
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.di.holder.NetworkLibAPI
import ru.snowadv.users_data.di.holder.UsersDataComponentHolder
import ru.snowadv.users_data.di.holder.UsersDataDependencies

internal object AuthorizedTestModulesInjector: BaseModulesInjector() {
    override fun inject(appContext: Context) {
        super.inject(appContext)

        TestAppModuleComponentHolder.dependencyProvider = {
            class TestAppModuleDependencyHolder(
                override val block: (BaseDependencyHolder<TestAppModuleDependencies>, AppModuleAPI) -> TestAppModuleDependencies
            ) : DependencyHolder1<AppModuleAPI, TestAppModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
            )

            TestAppModuleDependencyHolder { dependencyHolder, appApi ->
                object : TestAppModuleDependencies {
                    override val json: Json = appApi.json
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        AuthDataComponentHolder.dependencyProvider = {
            class AuthDataDependencyHolder(
                override val block: (BaseDependencyHolder<AuthDataDependencies>, TestAppModuleAPI) -> AuthDataDependencies
            ) : DependencyHolder1<TestAppModuleAPI, AuthDataDependencies>(
                api1 = TestAppModuleComponentHolder.get(),
            )


            AuthDataDependencyHolder { dependencyHolder, testApi ->
                object : AuthDataDependencies {
                    override val authStorageRepository: AuthStorageRepository = testApi.authStorageMock
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        AuthStorageComponentHolder.dependencyProvider = null


        ChatPresentationComponentHolder.dependencyProvider = {
            class ChatPresentationDependencyHolder(
                override val block: (BaseDependencyHolder<ChatPresentationDependencies>, AppModuleAPI, ChatDomainAPI, ImageLoaderLibAPI, TestAppModuleAPI, ChannelsDomainAPI) -> ChatPresentationDependencies
            ) : DependencyHolder5<AppModuleAPI, ChatDomainAPI, ImageLoaderLibAPI, TestAppModuleAPI, ChannelsDomainAPI, ChatPresentationDependencies>(
                api1 = AppModuleComponentHolder.get() ,
                api2 = ChatDomainComponentHolder.get(),
                api3 = ImageLoaderLibComponentHolder.get(),
                api4 = TestAppModuleComponentHolder.get(),
                api5 = ChannelsDomainComponentHolder.get(),
            )

            ChatPresentationDependencyHolder { dependencyHolder, appApi, chatApi, imageLoaderApi, testApi, channelsDomainApi ->
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
                    override val loadMessageUseCase: LoadMessageUseCase = chatApi.loadMessageUseCase
                    override val getTopicsUseCase: GetTopicsUseCase = channelsDomainApi.getTopicsUseCase
                    override val appContext: Context = appContext
                    override val imageLoader: ImageLoader = imageLoaderApi.coilImageLoader
                    override val baseUrlProvider: BaseUrlProvider = testApi.baseUrlProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }


        NetworkLibComponentHolder.dependencyProvider = {
            class NetworkLibDependencyHolder(
                override val block: (BaseDependencyHolder<NetworkLibDependencies>, AppModuleAPI, TestAppModuleAPI) -> NetworkLibDependencies
            ) : DependencyHolder2<AppModuleAPI, TestAppModuleAPI, NetworkLibDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = TestAppModuleComponentHolder.get(),
            )

            NetworkLibDependencyHolder { dependencyHolder, appApi, testApi ->
                object : NetworkLibDependencies {
                    override val badAuthBehavior: BadAuthBehavior = appApi.badAuthBehavior
                    override val authProvider: AuthProvider = testApi.authProviderMock
                    override val json: Json = appApi.json
                    override val baseUrlProvider: BaseUrlProvider = testApi.baseUrlProvider
                    override val loggerToggle: LoggerToggle = testApi.loggerToggle
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }


        ChatDataComponentHolder.dependencyProvider = {
            class ChatDataDependencyHolder(
                override val block: (BaseDependencyHolder<ChatDataDependencies>, NetworkLibAPI, AppModuleAPI, TestAppModuleAPI) -> ChatDataDependencies
            ) : DependencyHolder3<NetworkLibAPI, AppModuleAPI, TestAppModuleAPI, ChatDataDependencies>(
                api1 = NetworkLibComponentHolder.get(),
                api2 = AppModuleComponentHolder.get(),
                api3 = TestAppModuleComponentHolder.get(),
            )


            ChatDataDependencyHolder { dependencyHolder, networkApi, appApi, testApi ->
                object : ChatDataDependencies {
                    override val zulipApi: ZulipApi = networkApi.zulipApi
                    override val emojisDao: EmojisDao = testApi.emojisDao
                    override val messagesDao: MessagesDao = testApi.messagesDao
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val authProvider: AuthProvider = testApi.authProviderMock
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        EventsDataModuleComponentHolder.dependencyProvider = {
            class EventsDependencyHolder(
                override val block: (BaseDependencyHolder<EventsDataDependencies>, AppModuleAPI, NetworkLibAPI, TestAppModuleAPI) -> EventsDataDependencies

            ) : DependencyHolder3<AppModuleAPI, NetworkLibAPI, TestAppModuleAPI, EventsDataDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = TestAppModuleComponentHolder.get(),
            )


            EventsDependencyHolder { dependencyHolder, appApi, networkApi, testApi ->
                object : EventsDataDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = testApi.authProviderMock
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        UsersDataComponentHolder.dependencyProvider = {
            class UsersDataDependencyHolder(
                override val block: (BaseDependencyHolder<UsersDataDependencies>, NetworkLibAPI, AppModuleAPI, TestAppModuleAPI) -> UsersDataDependencies
            ) : DependencyHolder3<NetworkLibAPI, AppModuleAPI, TestAppModuleAPI, UsersDataDependencies>(
                api1 = NetworkLibComponentHolder.get(),
                api2 = AppModuleComponentHolder.get(),
                api3 = TestAppModuleComponentHolder.get(),
            )


            UsersDataDependencyHolder { dependencyHolder, networkApi, appApi, testApi ->
                object : UsersDataDependencies {
                    override val zulipApi: ZulipApi = networkApi.zulipApi
                    override val usersDao: UsersDao = testApi.usersDao
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val authProvider: AuthProvider = testApi.authProviderMock
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ChannelsDataComponentHolder.dependencyProvider = {
            class ChannelsDataDependencyHolder(
                override val block: (BaseDependencyHolder<ChannelsDataDependencies>, NetworkLibAPI, AppModuleAPI, TestAppModuleAPI) -> ChannelsDataDependencies
            ) : DependencyHolder3<NetworkLibAPI, AppModuleAPI, TestAppModuleAPI, ChannelsDataDependencies>(
                api1 = NetworkLibComponentHolder.get(),
                api2 = AppModuleComponentHolder.get(),
                api3 = TestAppModuleComponentHolder.get(),
            )


            ChannelsDataDependencyHolder { dependencyHolder, networkApi, appApi, testApi ->
                object : ChannelsDataDependencies {
                    override val zulipApi: ZulipApi = networkApi.zulipApi
                    override val streamsDao: StreamsDao = testApi.streamsDao
                    override val topicsDao: TopicsDao = testApi.topicsDao
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }



    }
}
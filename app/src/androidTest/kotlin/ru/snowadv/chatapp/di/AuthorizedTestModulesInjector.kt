package ru.snowadv.chatapp.di

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
import ru.snowadv.chat_domain_api.di.ChatDomainAPI
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
import ru.snowadv.chatapp.auth_mock.AuthProviderMock
import ru.snowadv.image_loader.di.holder.ImageLoaderLibAPI
import ru.snowadv.image_loader.di.holder.ImageLoaderLibComponentHolder
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.module_injector.dependency_holder.BaseDependencyHolder
import ru.snowadv.module_injector.dependency_holder.DependencyHolder0
import ru.snowadv.module_injector.dependency_holder.DependencyHolder2
import ru.snowadv.module_injector.dependency_holder.DependencyHolder3
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.network.api.BadAuthBehavior
import ru.snowadv.network.di.holder.NetworkLibComponentHolder
import ru.snowadv.network.di.holder.NetworkLibDependencies
import ru.snowadv.chatapp.config.WiremockBaseUrlProviderImpl
import ru.snowadv.chatapp.auth_mock.AuthStorageMockRepository
import ru.snowadv.chatapp.config.DebugLoggerToggle
import ru.snowadv.chatapp.di.holder.AppModuleAPI
import ru.snowadv.chatapp.di.holder.AppModuleComponentHolder
import ru.snowadv.chatapp.glue.injector.BaseModulesInjector
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.database.di.holder.DatabaseLibAPI
import ru.snowadv.database.di.holder.DatabaseLibComponentHolder
import ru.snowadv.events_impl.di.dagger.EventsDataModuleComponentHolder
import ru.snowadv.events_impl.di.holder.EventsDataDependencies
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.dependency_holder.DependencyHolder1
import ru.snowadv.network.api.LoggerToggle
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.di.holder.NetworkLibAPI
import ru.snowadv.users_data_api.di.UsersDataModuleDependencies
import ru.snowadv.users_data_impl.di.UsersDataModuleComponentHolder

internal object AuthorizedTestModulesInjector: BaseModulesInjector() {
    override fun inject(appContext: Context) {
        super.inject(appContext)

        AuthDataComponentHolder.dependencyProvider = {
            class AuthDataDependencyHolder(
                override val block: (BaseDependencyHolder<AuthDataDependencies>) -> AuthDataDependencies
            ) : DependencyHolder0<AuthDataDependencies>()


            AuthDataDependencyHolder { dependencyHolder ->
                object : AuthDataDependencies {
                    override val authStorageRepository: AuthStorageRepository = AuthStorageMockRepository
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        AuthStorageComponentHolder.dependencyProvider = null


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
                    override val baseUrlProvider: BaseUrlProvider = WiremockBaseUrlProviderImpl
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }


        NetworkLibComponentHolder.dependencyProvider = {
            class NetworkLibDependencyHolder(
                override val block: (BaseDependencyHolder<NetworkLibDependencies>, AppModuleAPI) -> NetworkLibDependencies
            ) : DependencyHolder1<AppModuleAPI, NetworkLibDependencies>(
                api1 = AppModuleComponentHolder.get(),
            )

            NetworkLibDependencyHolder { dependencyHolder, appApi ->
                object : NetworkLibDependencies {
                    override val badAuthBehavior: BadAuthBehavior = appApi.badAuthBehavior
                    override val authProvider: AuthProvider = AuthProviderMock
                    override val json: Json = appApi.json
                    override val baseUrlProvider: BaseUrlProvider = WiremockBaseUrlProviderImpl
                    override val loggerToggle: LoggerToggle = DebugLoggerToggle
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }


        ChatDataComponentHolder.dependencyProvider = {
            class ChatDataDependencyHolder(
                override val block: (BaseDependencyHolder<ChatDataDependencies>, NetworkLibAPI, DatabaseLibAPI, AppModuleAPI) -> ChatDataDependencies
            ) : DependencyHolder3<NetworkLibAPI, DatabaseLibAPI, AppModuleAPI, ChatDataDependencies>(
                api1 = NetworkLibComponentHolder.get(),
                api2 = DatabaseLibComponentHolder.get(),
                api3 = AppModuleComponentHolder.get(),
            )


            ChatDataDependencyHolder { dependencyHolder, networkApi,  dbApi, appApi ->
                object : ChatDataDependencies {
                    override val zulipApi: ZulipApi = networkApi.zulipApi
                    override val emojisDao: EmojisDao = dbApi.emojisDao
                    override val messagesDao: MessagesDao = dbApi.messagesDao
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val authProvider: AuthProvider = AuthProviderMock
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        EventsDataModuleComponentHolder.dependencyProvider = {
            class EventsDependencyHolder(
                override val block: (BaseDependencyHolder<EventsDataDependencies>, AppModuleAPI, NetworkLibAPI) -> EventsDataDependencies

            ) : DependencyHolder2<AppModuleAPI, NetworkLibAPI, EventsDataDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
            )


            EventsDependencyHolder { dependencyHolder, appApi, networkApi ->
                object : EventsDataDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = AuthProviderMock
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }

        UsersDataModuleComponentHolder.dependencyProvider = {
            class UsersDependenciesHolder(
                override val block: (BaseDependencyHolder<UsersDataModuleDependencies>, AppModuleAPI, NetworkLibAPI, DatabaseLibAPI) -> UsersDataModuleDependencies

            ) : DependencyHolder3<AppModuleAPI, NetworkLibAPI, DatabaseLibAPI, UsersDataModuleDependencies>(
                api1 = AppModuleComponentHolder.get(),
                api2 = NetworkLibComponentHolder.get(),
                api3 = DatabaseLibComponentHolder.get(),
            )


            UsersDependenciesHolder { dependencyHolder, appApi, networkApi, dbApi ->
                object : UsersDataModuleDependencies {
                    override val dispatcherProvider: DispatcherProvider = appApi.dispatcherProvider
                    override val api: ZulipApi = networkApi.zulipApi
                    override val authProvider: AuthProvider = AuthProviderMock
                    override val usersDao: UsersDao = dbApi.usersDao
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> =
                        dependencyHolder

                }
            }.dependencies
        }


    }
}
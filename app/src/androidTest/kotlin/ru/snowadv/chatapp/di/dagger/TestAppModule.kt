package ru.snowadv.chatapp.di.dagger

import com.github.tomakehurst.wiremock.common.ConsoleNotifier
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import kotlinx.serialization.json.Json
import ru.snowadv.auth_storage.provider.AuthProvider
import ru.snowadv.auth_storage.repository.AuthStorageRepository
import ru.snowadv.chatapp.mock.auth.AuthProviderMock
import ru.snowadv.chatapp.mock.auth.AuthStorageMockRepository
import ru.snowadv.chatapp.data.MockData
import ru.snowadv.chatapp.mock.config.DebugLoggerToggle
import ru.snowadv.chatapp.mock.config.WiremockBaseUrlProviderImpl
import ru.snowadv.chatapp.mock.dao.AuthDaoMockImpl
import ru.snowadv.chatapp.mock.dao.EmojisDaoMockImpl
import ru.snowadv.chatapp.mock.dao.MessagesDaoMockImpl
import ru.snowadv.chatapp.mock.dao.StreamsDaoMockImpl
import ru.snowadv.chatapp.mock.dao.TopicsDaoMockImpl
import ru.snowadv.chatapp.mock.dao.UsersDaoMockImpl
import ru.snowadv.chatapp.server.ChatServerResponseTransformer
import ru.snowadv.chatapp.server.wiremock.StaticStubsProvider
import ru.snowadv.chatapp.server.wiremock.api.WireMockStubsProvider
import ru.snowadv.database.dao.AuthDao
import ru.snowadv.database.dao.EmojisDao
import ru.snowadv.database.dao.MessagesDao
import ru.snowadv.database.dao.StreamsDao
import ru.snowadv.database.dao.TopicsDao
import ru.snowadv.database.dao.UsersDao
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.network.api.LoggerToggle
import wiremock.org.eclipse.jetty.util.thread.ExecutorThreadPool
import wiremock.org.eclipse.jetty.util.thread.ThreadPool
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
internal interface TestAppModule {
    @Binds
    fun bindAuthProviderMock(mock: AuthProviderMock): AuthProvider

    @Binds
    fun bindAuthStorageRepositoryMock(mock: AuthStorageMockRepository): AuthStorageRepository

    @Binds
    fun bindUrlProviderMock(mock: WiremockBaseUrlProviderImpl): BaseUrlProvider

    @Binds
    fun bindDebugLoggerToggle(toggle: DebugLoggerToggle): LoggerToggle

    @Binds
    fun bindStaticStubsProvider(provider: StaticStubsProvider): WireMockStubsProvider
    @Binds
    fun bindAuthDao(impl: AuthDaoMockImpl): AuthDao

    @Binds
    fun bindEmojisDao(impl: EmojisDaoMockImpl): EmojisDao

    @Binds
    fun bindMessagesDao(impl: MessagesDaoMockImpl): MessagesDao

    @Binds
    fun bindStreamsDao(impl: StreamsDaoMockImpl): StreamsDao

    @Binds
    fun bindTopicsDao(impl: TopicsDaoMockImpl): TopicsDao

    @Binds
    fun bindUsersDao(impl: UsersDaoMockImpl): UsersDao


    companion object {
        @Provides
        fun provideWireMockRule(transformer: ChatServerResponseTransformer): WireMockRule {
            return WireMockRule(
                WireMockConfiguration.wireMockConfig().extensions(transformer)
                    .asynchronousResponseEnabled(true)
                    .threadPoolFactory { ExecutorThreadPool(Executors.newFixedThreadPool(30)) }
                    .containerThreads(10)
                    .jettyAcceptors(5)
                    .asynchronousResponseThreads(10)
                    .notifier(ConsoleNotifier(true))
            )
        }
    }
}
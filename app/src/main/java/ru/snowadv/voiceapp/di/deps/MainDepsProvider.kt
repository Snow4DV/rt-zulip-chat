package ru.snowadv.voiceapp.di.deps

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.snowadv.auth_data.api.AuthDataRepository
import ru.snowadv.auth_data.impl.AuthDataRepositoryImpl
import ru.snowadv.channels_data.api.StreamDataRepository
import ru.snowadv.channels_data.api.TopicDataRepository
import ru.snowadv.channels_data.impl.StreamDataRepositoryImpl
import ru.snowadv.channels_data.impl.TopicDataRepositoryImpl
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.emojis_data.impl.EmojiDataRepositoryImpl
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.events_data.impl.EventDataRepositoryImpl
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.message_data.impl.MessageDataRepositoryImpl
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.properties_provider.api.AuthUserPropertyRepository
import ru.snowadv.properties_provider.impl.AuthUserPropertyRepositoryImpl
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.users_data.impl.UserDataRepositoryImpl
import ru.snowadv.voiceapp.glue.auth.AuthProviderImpl

internal class MainDepsProvider {
    // injectables
    lateinit var router: Router
    // lazy
    val api: ZulipApi by lazy { ZulipApi(authProvider = authProvider) }
    val streamDataRepository: StreamDataRepository by lazy { StreamDataRepositoryImpl(ioDispatcher = ioDispatcher, api = api) }
    val topicDataRepository: TopicDataRepository by lazy { TopicDataRepositoryImpl(ioDispatcher = ioDispatcher, api = api) }
    val eventDataRepository: EventRepository by lazy { EventDataRepositoryImpl(ioDispatcher = ioDispatcher, authProvider = authProvider, api = api) }
    val messageDataRepository: MessageDataRepository by lazy { MessageDataRepositoryImpl(ioDispatcher = ioDispatcher, authProvider = authProvider, api = api) }
    val userDataRepository: UserDataRepository by lazy { UserDataRepositoryImpl(ioDispatcher = ioDispatcher, api = api, authProvider = authProvider) }
    val emojiDataRepository: EmojiDataRepository by lazy { EmojiDataRepositoryImpl(ioDispatcher = ioDispatcher, api = api) }

    val authUserPropertyRepository: AuthUserPropertyRepository by lazy { AuthUserPropertyRepositoryImpl() }
    val authDataRepository: AuthDataRepository by lazy { AuthDataRepositoryImpl(authUserPropertyRepository) }

    val authProvider: AuthProvider by lazy { AuthProviderImpl(authDataRepository) }

    val defaultDispatcher by lazy { Dispatchers.Default }
    val mainDispatcher by lazy { Dispatchers.Main }
    val ioDispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }
}
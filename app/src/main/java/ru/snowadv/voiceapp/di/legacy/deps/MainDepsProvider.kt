package ru.snowadv.voiceapp.di.legacy.deps

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.snowadv.auth_data.api.AuthDataRepository
import ru.snowadv.auth_data.impl.AuthDataRepositoryImpl
import ru.snowadv.channels_data.api.StreamDataRepository
import ru.snowadv.channels_data.api.TopicDataRepository
import ru.snowadv.channels_data_impl.StreamDataRepositoryImpl
import ru.snowadv.channels_data_impl.TopicDataRepositoryImpl
import ru.snowadv.data.api.AuthProvider
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.emojis_data_impl.EmojiDataRepositoryImpl
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.events_data_impl.EventDataRepositoryImpl
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.messages_data_impl.MessageDataRepositoryImpl
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.properties_provider.api.AuthUserPropertyRepository
import ru.snowadv.properties_provider.impl.AuthUserPropertyRepositoryImpl
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.users_data_impl.UserDataRepositoryImpl
import ru.snowadv.voiceapp.glue.auth.AuthProviderImpl

internal class MainDepsProvider(val router: Router) {
    // lazy
    val api: ZulipApi by lazy { ZulipApi(authProvider = authProvider) }
    val streamDataRepository: StreamDataRepository by lazy {
        ru.snowadv.channels_data_impl.StreamDataRepositoryImpl(
            ioDispatcher = ioDispatcher,
            api = api
        )
    }
    val topicDataRepository: TopicDataRepository by lazy {
        ru.snowadv.channels_data_impl.TopicDataRepositoryImpl(
            ioDispatcher = ioDispatcher,
            api = api
        )
    }
    val eventDataRepository: EventRepository by lazy {
        ru.snowadv.events_data_impl.EventDataRepositoryImpl(
            ioDispatcher = ioDispatcher,
            authProvider = authProvider,
            api = api
        )
    }
    val messageDataRepository: MessageDataRepository by lazy {
        ru.snowadv.messages_data_impl.MessageDataRepositoryImpl(
            ioDispatcher = ioDispatcher,
            authProvider = authProvider,
            api = api
        )
    }
    val userDataRepository: UserDataRepository by lazy {
        ru.snowadv.users_data_impl.UserDataRepositoryImpl(
            ioDispatcher = ioDispatcher,
            api = api,
            authProvider = authProvider
        )
    }
    val emojiDataRepository: EmojiDataRepository by lazy {
        ru.snowadv.emojis_data_impl.EmojiDataRepositoryImpl(
            ioDispatcher = ioDispatcher,
            api = api
        )
    }

    val authUserPropertyRepository: AuthUserPropertyRepository by lazy { AuthUserPropertyRepositoryImpl() }
    val authDataRepository: AuthDataRepository by lazy { AuthDataRepositoryImpl(authUserPropertyRepository) }

    val authProvider: AuthProvider by lazy { AuthProviderImpl(authDataRepository) }

    val defaultDispatcher by lazy { Dispatchers.Default }
    val mainDispatcher by lazy { Dispatchers.Main }
    val ioDispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }
}
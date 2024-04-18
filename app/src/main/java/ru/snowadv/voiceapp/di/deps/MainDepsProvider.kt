package ru.snowadv.voiceapp.di.deps

import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import ru.snowadv.channels_data.api.StreamDataRepository
import ru.snowadv.channels_data.api.TopicDataRepository
import ru.snowadv.channels_data.impl.StreamDataRepositoryImpl
import ru.snowadv.channels_data.impl.TopicDataRepositoryImpl
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.emojis_data.impl.EmojiDataRepositoryImpl
import ru.snowadv.events_data.api.EventDataRepository
import ru.snowadv.events_data.impl.EventDataRepositoryImpl
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.message_data.impl.MessageDataRepositoryImpl
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.users_data.impl.UserDataRepositoryImpl

internal class MainDepsProvider {
    // injectables
    lateinit var router: Router
    // lazy
    val ioDispatcher: CoroutineDispatcher by lazy { Dispatchers.IO }
    val streamDataRepository: StreamDataRepository by lazy { StreamDataRepositoryImpl(ioDispatcher = ioDispatcher) }
    val topicDataRepository: TopicDataRepository by lazy { TopicDataRepositoryImpl(ioDispatcher = ioDispatcher) }
    val eventDataRepository: EventDataRepository by lazy { EventDataRepositoryImpl(ioDispatcher = ioDispatcher) }
    val messageDataRepository: MessageDataRepository by lazy { MessageDataRepositoryImpl(ioDispatcher = ioDispatcher) }
    val userDataRepository: UserDataRepository by lazy { UserDataRepositoryImpl(ioDispatcher = ioDispatcher) }
    val emojiDataRepository: EmojiDataRepository by lazy { EmojiDataRepositoryImpl(ioDispatcher = ioDispatcher) }
}
package ru.snowadv.voiceapp.di.holder

import android.content.Context
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.users_data_api.UserDataRepository

interface AppModuleDependencies : BaseModuleDependencies {
    val authDataRepository: AuthDataRepository

    val streamDataRepository: StreamDataRepository
    val topicDataRepository: TopicDataRepository

    val emojiDataRepository: EmojiDataRepository

    val eventRepository: EventRepository

    val messageDataRepository: MessageDataRepository

    val userDataRepository: UserDataRepository

    val appContext: Context
}
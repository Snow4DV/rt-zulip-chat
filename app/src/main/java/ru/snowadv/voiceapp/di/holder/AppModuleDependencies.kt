package ru.snowadv.voiceapp.di.holder

import android.content.Context
import ru.snowadv.auth_data.api.AuthDataRepository
import ru.snowadv.channels_data.api.StreamDataRepository
import ru.snowadv.channels_data.api.TopicDataRepository
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.users_data.api.UserDataRepository

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
package ru.snowadv.voiceapp.di.holder

import android.content.Context
import ru.snowadv.auth_data_api.AuthDataRepository
import ru.snowadv.channels_api.presentation.ChannelsScreenFactory
import ru.snowadv.channels_data_api.StreamDataRepository
import ru.snowadv.channels_data_api.TopicDataRepository
import ru.snowadv.chat_api.presentation.ChatScreenFactory
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.events_data_api.EventRepository
import ru.snowadv.home_api.presentation.feature.HomeScreenFactory
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.people_api.presentation.PeopleScreenFactory
import ru.snowadv.profile_api.presentation.ProfileScreenFactory
import ru.snowadv.users_data_api.UserDataRepository

interface AppModuleDependencies : BaseModuleDependencies {
    val authDataRepository: AuthDataRepository
    val appContext: Context
}
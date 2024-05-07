package ru.snowadv.chat_api.di

import android.content.Context
import coil.ImageLoader
import ru.snowadv.chat_api.domain.navigation.ChatRouter
import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.events_api.domain.EventRepository
import ru.snowadv.messages_data_api.MessageDataRepository
import ru.snowadv.model.DispatcherProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChatFeatureDependencies : BaseModuleDependencies {
    val router: ChatRouter
    val emojiDataRepo: EmojiDataRepository
    val messageDataRepo: MessageDataRepository
    val eventRepository: EventRepository
    val dispatcherProvider: DispatcherProvider
    val appContext: Context
    val coilImageLoader: ImageLoader
}
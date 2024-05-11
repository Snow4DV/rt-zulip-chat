package ru.snowadv.chat_presentation.di.holder

import android.content.Context
import coil.ImageLoader
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChatPresentationDependencies : BaseModuleDependencies {
    val chatRouter: ChatRouter

    val addReactionUseCase: AddReactionUseCase
    val removeReactionUseCase: RemoveReactionUseCase
    val sendMessageUseCase: SendMessageUseCase
    val getMessagesUseCase: GetCurrentMessagesUseCase
    val listenToChatEventsUseCase: ListenToChatEventsUseCase
    val loadMoreMessagesUseCase: LoadMoreMessagesUseCase
    val sendFileUseCase: SendFileUseCase
    val getEmojisUseCase: GetEmojisUseCase

    val appContext: Context
    val imageLoader: ImageLoader
}
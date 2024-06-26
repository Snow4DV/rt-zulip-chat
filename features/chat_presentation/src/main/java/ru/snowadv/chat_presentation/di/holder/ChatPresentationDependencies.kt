package ru.snowadv.chat_presentation.di.holder

import android.content.Context
import coil.ImageLoader
import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.ChangeMessageReadStateUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_presentation.navigation.ChatRouter
import ru.snowadv.message_actions_presentation.api.screen_factory.ActionChooserDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.EmojiChooserDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageEditorDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageTopicChangerDialogFactory
import ru.snowadv.model.BaseUrlProvider
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface ChatPresentationDependencies : BaseModuleDependencies {
    val chatRouter: ChatRouter
    val actionChooserDialogFactory: ActionChooserDialogFactory
    val emojiChooserDialogFactory: EmojiChooserDialogFactory
    val messageEditorDialogFactory: MessageEditorDialogFactory
    val messageTopicChangerDialogFactory: MessageTopicChangerDialogFactory

    val addReactionUseCase: AddReactionUseCase
    val removeReactionUseCase: RemoveReactionUseCase
    val sendMessageUseCase: SendMessageUseCase
    val getMessagesUseCase: GetCurrentMessagesUseCase
    val listenToChatEventsUseCase: ListenToChatEventsUseCase
    val loadMoreMessagesUseCase: LoadMoreMessagesUseCase
    val sendFileUseCase: SendFileUseCase
    val getEmojisUseCase: GetEmojisUseCase
    val loadMessageUseCase: LoadMessageUseCase
    val markMessagesAsReadUseCase: ChangeMessageReadStateUseCase

    val getTopicsUseCase: GetTopicsUseCase

    val appContext: Context
    val imageLoader: ImageLoader
    val baseUrlProvider: BaseUrlProvider
}
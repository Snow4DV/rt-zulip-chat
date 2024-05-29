package ru.snowadv.chat_domain_api.di

import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.EditMessageUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.ChangeMessageReadStateUseCase
import ru.snowadv.chat_domain_api.use_case.MoveMessageToOtherTopicUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveMessageUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.module_injector.module.BaseModuleAPI

interface ChatDomainAPI : BaseModuleAPI {
    val addReactionUseCase: AddReactionUseCase
    val getCurrentMessagesUseCase: GetCurrentMessagesUseCase
    val getEmojisUseCase: GetEmojisUseCase
    val listenToChatEventsUseCase: ListenToChatEventsUseCase
    val loadMoreMessagesUseCase: LoadMoreMessagesUseCase
    val loadMessageUseCase: LoadMessageUseCase
    val removeReactionUseCase: RemoveReactionUseCase
    val sendFileUseCase: SendFileUseCase
    val sendMessageUseCase: SendMessageUseCase
    val editMessageUseCase: EditMessageUseCase
    val moveMessageToOtherTopicUseCase: MoveMessageToOtherTopicUseCase
    val removeMessageUseCase: RemoveMessageUseCase
    val markMessagesAsReadUseCase: ChangeMessageReadStateUseCase
}
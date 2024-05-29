package ru.snowadv.message_actions_presentation.di.holder

import ru.snowadv.channels_domain_api.use_case.GetTopicsUseCase
import ru.snowadv.chat_domain_api.use_case.EditMessageUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.MoveMessageToOtherTopicUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveMessageUseCase
import ru.snowadv.message_actions_presentation.navigation.MessageActionsRouter
import ru.snowadv.module_injector.module.BaseModuleDependencies

interface MessageActionsPresentationDependencies : BaseModuleDependencies {
    val getEmojisUseCase: GetEmojisUseCase
    val getTopicsUseCase: GetTopicsUseCase
    val loadMessageUseCase: LoadMessageUseCase
    val messageActionsRouter: MessageActionsRouter
    val removeMessageUseCase: RemoveMessageUseCase
    val editMessageUseCase: EditMessageUseCase
    val moveMessageToOtherTopicUseCase: MoveMessageToOtherTopicUseCase
}
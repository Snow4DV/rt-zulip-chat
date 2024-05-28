package ru.snowadv.chat_presentation.chat.presentation.elm

sealed interface ChatEffectElm {
    data class OpenReactionChooser(val destMessageId: Long): ChatEffectElm
    data class ShowActionErrorWithRetry(val retryEvent: ChatEventElm): ChatEffectElm
    data object ShowActionError : ChatEffectElm
    data class OpenMessageActionsChooser(val messageId: Long, val userId: Long): ChatEffectElm
    data object OpenFileChooser : ChatEffectElm
}


package ru.snowadv.chat_impl.presentation.chat.elm

internal sealed interface ChatEffectElm {
    data class OpenReactionChooser(val destMessageId: Long): ChatEffectElm
    data class ShowActionErrorWithRetry(val retryEvent: ChatEventElm): ChatEffectElm
    data object ShowActionError : ChatEffectElm
    data object ExplainNotImplemented: ChatEffectElm
    data object ExplainReactionAlreadyExists: ChatEffectElm
    data object ScrollRecyclerToTheEnd: ChatEffectElm
    data class OpenMessageActionsChooser(val messageId: Long, val userId: Long): ChatEffectElm
}


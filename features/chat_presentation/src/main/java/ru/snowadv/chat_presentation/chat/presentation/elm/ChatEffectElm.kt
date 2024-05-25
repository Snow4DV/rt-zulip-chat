package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.chat_presentation.chat.ui.elm.ChatEffectUiElm

sealed interface ChatEffectElm {
    data class OpenReactionChooser(val destMessageId: Long, val excludeEmojisNames: List<String>): ChatEffectElm
    data class ShowActionErrorWithRetry(val retryEvent: ChatEventElm): ChatEffectElm
    data object ShowActionError : ChatEffectElm
    data class OpenMessageActionsChooser(val messageId: Long, val userId: Long): ChatEffectElm
    data object OpenFileChooser : ChatEffectElm
    data object ShowTopicChangedBecauseNewMessageIsUnreachable : ChatEffectElm
}


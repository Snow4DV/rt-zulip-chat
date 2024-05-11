package ru.snowadv.chat_presentation.chat.ui.elm

import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm

internal sealed interface ChatEffectUiElm {
    data class OpenReactionChooser(val destMessageId: Long): ChatEffectUiElm
    data class ShowActionErrorWithRetry(val retryEvent: ChatEventElm): ChatEffectUiElm
    data object ShowActionError : ChatEffectUiElm
    data class OpenMessageActionsChooser(val messageId: Long, val userId: Long): ChatEffectUiElm
    data object OpenFileChooser : ChatEffectUiElm
}


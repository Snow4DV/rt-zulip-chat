package ru.snowadv.chat_presentation.chat.presentation.elm

import ru.snowadv.chat_presentation.chat.presentation.model.SnackbarText
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEffectUiElm

sealed interface ChatEffectElm {
    data class OpenReactionChooser(val destMessageId: Long, val excludeEmojisCodes: List<String>): ChatEffectElm
    data class ShowActionErrorWithRetry(val retryEvent: ChatEventElm): ChatEffectElm
    data class ShowSnackbarWithText(val text: SnackbarText) : ChatEffectElm
    data class OpenMessageActionsChooser(val messageId: Long, val userId: Long, val streamName: String, val isOwner: Boolean): ChatEffectElm
    data object OpenFileChooser : ChatEffectElm
    data object ShowTopicChangedBecauseNewMessageIsUnreachable : ChatEffectElm
    data class OpenMessageEditor(val messageId: Long, val streamName: String) : ChatEffectElm
    data class OpenMessageTopicChanger(val messageId: Long, val streamId: Long, val topicName: String) : ChatEffectElm
}


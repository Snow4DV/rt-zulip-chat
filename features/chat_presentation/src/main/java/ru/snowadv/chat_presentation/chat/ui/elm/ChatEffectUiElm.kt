package ru.snowadv.chat_presentation.chat.ui.elm

import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEffectElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.model.SnackbarText
import ru.snowadv.chat_presentation.chat.ui.model.SnackbarUiText

internal sealed interface ChatEffectUiElm {
    data class OpenReactionChooser(val destMessageId: Long, val excludeEmojis: List<String>): ChatEffectUiElm
    data class ShowActionErrorWithRetry(val retryEvent: ChatEventElm): ChatEffectUiElm
    data class ShowSnackbarWithText(val text: SnackbarUiText) : ChatEffectUiElm
    data class OpenMessageActionsChooser(val messageId: Long, val userId: Long, val streamName: String, val isOwner: Boolean): ChatEffectUiElm
    data object OpenFileChooser : ChatEffectUiElm
    data object ShowTopicChangedBecauseNewMessageIsUnreachable : ChatEffectUiElm
    data object ExpandTopicChooser : ChatEffectUiElm
    data class OpenMessageEditor(val messageId: Long, val streamName: String) : ChatEffectUiElm
    data class OpenMessageTopicChanger(val messageId: Long, val streamId: Long, val topicName: String) : ChatEffectUiElm
    data class RefreshMessageWithId(val messageId: Long) : ChatEffectUiElm
}


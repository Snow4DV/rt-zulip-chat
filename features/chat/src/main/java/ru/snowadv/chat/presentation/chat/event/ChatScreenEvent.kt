package ru.snowadv.chat.presentation.chat.event

import ru.snowadv.chat.domain.model.Emoji
import ru.snowadv.chat.presentation.model.ChatEmoji

internal sealed class ChatScreenEvent {
    class TextFieldMessageChanged(val text: String): ChatScreenEvent()
    class SendButtonClicked(val currentMessageFieldText: String): ChatScreenEvent()
    data object AddAttachmentButtonClicked: ChatScreenEvent()
    data class AddReactionClicked(val messageId: Long): ChatScreenEvent()
    data class AddChosenReaction(val messageId: Long, val reactionName: String): ChatScreenEvent()
    data class RemoveReaction(val messageId: Long, val reactionName: String): ChatScreenEvent()
}
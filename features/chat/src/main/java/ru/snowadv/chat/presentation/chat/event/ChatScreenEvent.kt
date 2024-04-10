package ru.snowadv.chat.presentation.chat.event

internal sealed class ChatScreenEvent {
    class TextFieldMessageChanged(val text: String): ChatScreenEvent()
    data object SendMessageAddAttachmentButtonClicked: ChatScreenEvent()
    class MessageLongClicked(val messageId: Long, val userId: Long): ChatScreenEvent()
    class AddReactionClicked(val messageId: Long): ChatScreenEvent()
    class AddChosenReaction(val messageId: Long, val reactionName: String): ChatScreenEvent()
    class RemoveReaction(val messageId: Long, val reactionName: String): ChatScreenEvent()
    class GoToProfileClicked(val profileId: Long): ChatScreenEvent()
    data object GoBackClicked: ChatScreenEvent()
    data object ReloadClicked: ChatScreenEvent()
}
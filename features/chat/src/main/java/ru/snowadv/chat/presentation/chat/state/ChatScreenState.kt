package ru.snowadv.chat.presentation.chat.state

import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatPaginationStatus
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.chat.presentation.util.mapToAdapterMessagesAndDates
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class ChatScreenState(
    val sendingMessage: Boolean = false,
    val changingReaction: Boolean = false,
    val stream: String,
    val topic: String,
    val screenState: ScreenState<List<DelegateItem>> = ScreenState.Loading,
    val messages: List<ChatMessage> = emptyList(),
    val messageField: String = "",
    val actionButtonType: ActionButtonType = ActionButtonType.ADD_ATTACHMENT,
    val paginationStatus: ChatPaginationStatus = ChatPaginationStatus.HasMore,
) {

    val paginatedScreenState = screenState.map { messagesDelegates ->
        when (paginationStatus) {
            ChatPaginationStatus.None, ChatPaginationStatus.LoadedAll -> messagesDelegates
            ChatPaginationStatus.Loading, ChatPaginationStatus.HasMore, ChatPaginationStatus.Error -> buildList {
                add(paginationStatus)
                addAll(messagesDelegates)
            }
        }
    }

    val firstLoadedMessageId: Long? = messages.firstOrNull()?.id

    val isActionButtonVisible = !screenState.isLoading && !sendingMessage

    enum class ActionButtonType {
        SEND_MESSAGE,
        ADD_ATTACHMENT,
    }

    fun addReaction(emoji: ChatEmoji, messageId: Long, isByCurrentUser: Boolean): ChatScreenState {
        val oldReaction = messages.firstOrNull { it.id == messageId }?.reactions
            ?.firstOrNull { it.emojiCode == emoji.code }
            ?: ChatReaction(
                name = emoji.name,
                emojiCode = emoji.code,
                count = 0,
                userReacted = isByCurrentUser,
                emojiString = emoji.convertedEmojiString,
            )
        return updateOrAddReactionToMessage(
            newReaction = oldReaction.copy(
                count = oldReaction.count + 1,
                userReacted = oldReaction.userReacted || isByCurrentUser
            ),
            messageId = messageId
        )
    }

    fun removeReaction(
        emoji: ChatEmoji,
        messageId: Long,
        isByCurrentUser: Boolean
    ): ChatScreenState {
        val oldReaction = messages.firstOrNull { it.id == messageId }?.reactions
            ?.firstOrNull { it.emojiCode == emoji.code }
            ?: ChatReaction(
                name = emoji.name,
                emojiCode = emoji.code,
                count = 0,
                userReacted = isByCurrentUser,
                emojiString = emoji.convertedEmojiString,
            )

        return updateOrAddReactionToMessage(
            newReaction = oldReaction.copy(
                count = oldReaction.count - 1,
                userReacted = oldReaction.userReacted && !isByCurrentUser
            ),
            messageId = messageId
        )
    }

    private fun updateOrAddReactionToMessage(
        newReaction: ChatReaction,
        messageId: Long
    ): ChatScreenState {
        return updateMessageList(messages.map { message ->
            if (message.id == messageId) {
                val reactionExists: Boolean =
                    message.reactions.any { it.emojiCode == newReaction.emojiCode }
                message.copy(
                    reactions = (if (reactionExists) {
                        message.reactions.map { reaction ->
                            if (reaction.emojiCode == newReaction.emojiCode) {
                                newReaction
                            } else {
                                reaction
                            }
                        }
                    } else {
                        message.reactions + newReaction
                    }).filter { it.count > 0 }
                )
            } else {
                message
            }
        })
    }

    fun addMessage(newMessage: ChatMessage): ChatScreenState {
        return updateMessageList(messages + newMessage)
    }

    fun replaceMessage(newMessage: ChatMessage): ChatScreenState {
        return updateMessageList(messages.map { if (it.id == newMessage.id) newMessage else it })
    }

    fun updateMessage(messageId: Long, renderedContent: String): ChatScreenState {
        return updateMessageList(messages.map { if (it.id == messageId) it.copy(text = renderedContent) else it })
    }

    fun removeMessage(messageId: Long): ChatScreenState {
        return updateMessageList(messages.filter { it.id != messageId })
    }

    private fun updateMessageList(messageList: List<ChatMessage>): ChatScreenState {
        // messages can only be updated after initial fetch
        if (screenState is ScreenState.Error || screenState is ScreenState.Loading) return this

        return copy(
            screenState = ScreenState.Success(messageList.mapToAdapterMessagesAndDates()),
            messages = messageList,
        )
    }
}
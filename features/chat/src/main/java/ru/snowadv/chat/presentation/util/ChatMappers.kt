package ru.snowadv.chat.presentation.util

import ru.snowadv.chat.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.chat.domain.model.ChatEmoji as ModelChatEmoji
import ru.snowadv.chat.domain.model.ChatReaction as DomainChatReaction
import ru.snowadv.chat.presentation.model.ChatEmoji as UiChatEmoji
import ru.snowadv.chat.domain.model.ChatMessage as DomainChatMessage
import ru.snowadv.chat.presentation.model.ChatMessage
import ru.snowadv.chat.presentation.model.ChatMessageType
import ru.snowadv.chat.presentation.model.ChatReaction
import ru.snowadv.event_api.model.DomainEvent
import ru.snowadv.event_api.model.EventMessage
import ru.snowadv.event_api.model.EventReaction
import ru.snowadv.presentation.util.toLocalDateTimeWithCurrentZone
import ru.snowadv.utils.EmojiUtils

internal object ChatMappers {

    fun DomainChatMessage.toUiChatMessage(): ChatMessage {
        return ChatMessage(
            id = id,
            text = content,
            sentAt = sentAt.toLocalDateTimeWithCurrentZone(),
            senderId = senderId,
            senderName = senderName,
            senderAvatarUrl = senderAvatarUrl,
            reactions = reactions.map { it.toUiChatReaction() },
            messageType = if (owner) ChatMessageType.OUTGOING else ChatMessageType.INCOMING
        )
    }

    private fun DomainChatReaction.toUiChatReaction(): ChatReaction {
        return ChatReaction(
            name = name,
            emojiCode = emojiCode,
            count = count,
            userReacted = userReacted,
            emojiString = EmojiUtils.combinedHexToString(emojiCode),
        )
    }

    fun ModelChatEmoji.toUiChatEmoji(): UiChatEmoji {
        return UiChatEmoji(
            name = name,
            code = code,
            convertedEmojiString = EmojiUtils.combinedHexToString(code)
        )
    }

    private fun EventReaction.toChatReaction(): ChatReaction {
        return ChatReaction(
            name = name,
            emojiCode = emojiCode,
            count = count,
            userReacted = userReacted,
            emojiString = EmojiUtils.combinedHexToString(emojiCode)
        )
    }

    fun DomainEvent.ReactionDomainEvent.toUiChatEmoji(): ChatEmoji {
        return ChatEmoji(
            name = emojiName,
            code = emojiCode,
            convertedEmojiString = EmojiUtils.combinedHexToString(emojiCode)
        )
    }

    fun EventMessage.toUiChatMessage(): ChatMessage {
        return ChatMessage(
            id = id,
            text = content,
            sentAt = sentAt.toLocalDateTimeWithCurrentZone(),
            senderId = senderId,
            senderName = senderFullName,
            senderAvatarUrl = avatarUrl,
            reactions = reactions.map { it.toChatReaction() },
            messageType = if (owner) ChatMessageType.OUTGOING else ChatMessageType.INCOMING
        )
    }

    fun DomainEvent.toElmEvent(): ChatEventElm {
        return when(this) {
            is DomainEvent.DeleteMessageDomainEvent -> ChatEventElm.Internal.ServerEvent.MessageDeleted(
                eventId = id,
                queueId = queueId,
                messageId = messageId,
            )
            is DomainEvent.FailedFetchingQueueEvent -> ChatEventElm.Internal.ServerEvent.EventQueueFailed(
                queueId = queueId,
                eventId = id,
                recreateQueue = isQueueBad,
            )
            is DomainEvent.MessageDomainEvent -> ChatEventElm.Internal.ServerEvent.NewMessage(
                queueId = queueId,
                eventId = id,
                message = eventMessage.toUiChatMessage(),
            )
            is DomainEvent.ReactionDomainEvent -> this.toElmEvent()
            is DomainEvent.RegisteredNewQueueEvent -> ChatEventElm.Internal.ServerEvent.EventQueueRegistered(
                queueId = queueId,
                eventId = id,
                timeoutSeconds = timeoutSeconds,
            )
            is DomainEvent.UpdateMessageDomainEvent -> ChatEventElm.Internal.ServerEvent.MessageUpdated(
                queueId = queueId,
                eventId = id,
                messageId = messageId,
                newContent = content,
            )
            else -> ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
                queueId = queueId,
                eventId = id,
            )
        }
    }

    fun DomainEvent.ReactionDomainEvent.toElmEvent(): ChatEventElm {
        return when(op) {
            DomainEvent.ReactionDomainEvent.OperationType.ADD -> ChatEventElm.Internal.ServerEvent.ReactionAdded(
                queueId = queueId,
                eventId = id,
                messageId = messageId,
                emoji = toUiChatEmoji(),
                currentUserReaction = currentUserReaction,
            )
            DomainEvent.ReactionDomainEvent.OperationType.REMOVE -> ChatEventElm.Internal.ServerEvent.ReactionRemoved(
                queueId = queueId,
                eventId = id,
                messageId = messageId,
                emoji = toUiChatEmoji(),
                currentUserReaction = currentUserReaction,
            )
        }
    }

}
package ru.snowadv.chat_presentation.chat.ui.util

import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.presentation.model.SnackbarText
import ru.snowadv.chat_presentation.chat.ui.elm.ChatStateUiElm
import ru.snowadv.chat_domain_api.model.ChatReaction as DomainChatReaction
import ru.snowadv.chat_domain_api.model.ChatMessage as DomainChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus as DomainPaginationStatus
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessage
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessageType
import ru.snowadv.chat_presentation.chat.ui.model.ChatPaginationStatus
import ru.snowadv.chat_presentation.chat.ui.model.ChatReaction
import ru.snowadv.chat_presentation.chat.ui.model.SnackbarUiText
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventMessage
import ru.snowadv.events_api.model.EventReaction
import ru.snowadv.presentation.util.toLocalDateTimeWithCurrentZone
import ru.snowadv.utils.EmojiUtils

internal object ChatMappers {

    fun ChatStateElm.ActionButtonType.toUiType(): ChatStateUiElm.ActionButtonType {
        return ChatStateUiElm.ActionButtonType.valueOf(toString())
    }

    fun DomainChatMessage.toUiChatMessage(): ChatMessage {
        return ChatMessage(
            id = id,
            text = content,
            sentAt = sentAt.toLocalDateTimeWithCurrentZone(),
            senderId = senderId,
            senderName = senderName,
            senderAvatarUrl = senderAvatarUrl,
            reactions = reactions.map { it.toUiChatReaction() }
                .sortedWith(compareBy({ -it.count }, { it.name })),
            messageType = if (owner) ChatMessageType.OUTGOING else ChatMessageType.INCOMING,
            topic = topic,
            isRead = isRead,
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


    private fun DomainEvent.ReactionDomainEvent.toDomainChatEmoji(): ChatEmoji {
        return ChatEmoji(
            name = emojiName,
            code = emojiCode,
        )
    }

    private fun EventReaction.toChatReaction(): DomainChatReaction {
        return DomainChatReaction(
            name = name,
            emojiCode = emojiCode,
            count = count,
            userReacted = userReacted,
        )
    }



    fun DomainPaginationStatus.toUiPaginationStatus(): ChatPaginationStatus {
        return when(this) {
            ru.snowadv.chat_domain_api.model.ChatPaginationStatus.Error -> ChatPaginationStatus.Error
            ru.snowadv.chat_domain_api.model.ChatPaginationStatus.HasMore -> ChatPaginationStatus.HasMore
            ru.snowadv.chat_domain_api.model.ChatPaginationStatus.LoadedAll -> ChatPaginationStatus.LoadedAll
            ru.snowadv.chat_domain_api.model.ChatPaginationStatus.Loading -> ChatPaginationStatus.Loading
            ru.snowadv.chat_domain_api.model.ChatPaginationStatus.None -> ChatPaginationStatus.None
        }
    }

    fun DomainEvent.toElmEvent(): ChatEventElm {
        return when (this) {
            is DomainEvent.DeleteMessageDomainEvent -> ChatEventElm.Internal.ServerEvent.MessageDeleted(
                eventId = id,
                queueId = queueId,
                messageId = messageId,
            )

            is DomainEvent.FailedFetchingQueueEvent -> ChatEventElm.Internal.ServerEvent.EventQueueFailed(
                queueId = queueId,
                eventId = id,
                recreateQueue = isQueueBad,
                reason = reason,
            )

            is DomainEvent.MessageDomainEvent -> ChatEventElm.Internal.ServerEvent.NewMessage(
                queueId = queueId,
                eventId = id,
                message = eventMessage.toChatMessage(),
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
                newSubject = subject,
            )

            is DomainEvent.AddReadMessageFlagEvent -> ChatEventElm.Internal.ServerEvent.MessagesRead(
                queueId = queueId,
                eventId = id,
                addFlagMessagesIds = addFlagMessagesIds,
            )

            is DomainEvent.RemoveReadMessageFlagEvent -> ChatEventElm.Internal.ServerEvent.MessagesUnread(
                queueId = queueId,
                eventId = id,
                removeFlagMessagesIds = removeFlagMessagesIds,
            )

            else -> ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
                queueId = queueId,
                eventId = id,
            )
        }
    }

    private fun EventMessage.toChatMessage(): DomainChatMessage {
        return DomainChatMessage(
            id = id,
            content = content,
            sentAt = sentAt,
            senderId = senderId,
            senderName = senderFullName,
            senderAvatarUrl = avatarUrl,
            reactions = reactions.map { it.toChatReaction() },
            owner = this.owner,
            topic = subject,
            isRead = "read" in flags,
        )
    }

    private fun DomainEvent.ReactionDomainEvent.toElmEvent(): ChatEventElm {
        return when (op) {
            DomainEvent.ReactionDomainEvent.OperationType.ADD -> ChatEventElm.Internal.ServerEvent.ReactionAdded(
                queueId = queueId,
                eventId = id,
                messageId = messageId,
                emoji = toDomainChatEmoji(),
                currentUserReaction = currentUserReaction,
            )

            DomainEvent.ReactionDomainEvent.OperationType.REMOVE -> ChatEventElm.Internal.ServerEvent.ReactionRemoved(
                queueId = queueId,
                eventId = id,
                messageId = messageId,
                emoji = toDomainChatEmoji(),
                currentUserReaction = currentUserReaction,
            )
        }
    }

    fun SnackbarText.toUiModel(): SnackbarUiText {
        return SnackbarUiText.valueOf(toString())
    }
}
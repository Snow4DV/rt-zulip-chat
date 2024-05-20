package ru.snowadv.chat_presentation.chat.presentation.elm.data

import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatReaction
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.events_api.model.DomainEvent
import ru.snowadv.events_api.model.EventMessage
import ru.snowadv.events_api.model.EventPresence
import ru.snowadv.events_api.model.EventReaction
import java.io.IOException
import java.time.ZonedDateTime

internal class ChatActorElmTestData {
    val streamName = "stream"
    val streamId = 1
    val topicName = "topic"
    val reactionType = "unicode_reaction"

    val messageAddedAfterCaching = ChatMessage(
        id = 2,
        content = "Other text",
        sentAt = ZonedDateTime.now().plusDays(1),
        senderId = 555,
        senderName = "Petr Petrov",
        senderAvatarUrl = "http://example.com/someurl.png",
        reactions = emptyList(),
        owner = true
    )

    val sampleZonedDateTime = ZonedDateTime.now()
    val sampleZonedDateTimes = List(5) {
        sampleZonedDateTime.plusDays(it.toLong())
    }

    val initialCachedMessage = ChatMessage(
        id = 1,
        content = "Some text",
        sentAt = ZonedDateTime.now(),
        senderId = 123,
        senderName = "Ivan Ivanov",
        senderAvatarUrl = null,
        reactions = emptyList(),
        owner = false
    )

    val remoteMessages = listOf(
        initialCachedMessage,
        messageAddedAfterCaching,
    )

    val cachedMessages = listOf(
        initialCachedMessage,
    )

    val emojis = listOf(
        ChatEmoji("happy", "1f600"),
        ChatEmoji("unhappy", "1f601"),
        ChatEmoji("sad", "1f602"),
    )

    val sampleEmoji = emojis.first()

    val sampleText = "sample text"

    val newEmoji = ChatEmoji("blush", "1f603")

    val updatedEmojis = emojis + newEmoji

    val secondPageMessage = ChatMessage(
        id = 3,
        content = "Third text",
        sentAt = ZonedDateTime.now().plusDays(2),
        senderId = 550,
        senderName = "Vladimir Vladimirov",
        senderAvatarUrl = "http://example.com/someurl.png",
        reactions = listOf(ChatReaction(sampleEmoji.name, sampleEmoji.code, 1, false)),
        owner = false
    )

    val secondPageMessages = listOf(secondPageMessage)

    val events = listOf(
        DomainEvent.FailedFetchingQueueEvent(
            id = -1,
            queueId = null,
            isQueueBad = true,
            reason = IOException()
        ),
        DomainEvent.RegisteredNewQueueEvent(
            queueId = "new_queue",
            id = 0,
            timeoutSeconds = 60,
        ),
        DomainEvent.MessageDomainEvent(
            id = 1,
            eventMessage = EventMessage(
                id = 1,
                content = "Example content",
                sentAt = sampleZonedDateTime,
                senderId = 123,
                senderFullName = "John Doe",
                avatarUrl = "http://example.com/1.png",
                reactions = listOf(EventReaction(sampleEmoji.name, sampleEmoji.code, 1, false, reactionType)),
                owner = false,
                flags = setOf("read"),
                type = "stream",
                streamId = 1,
                subject = topicName,
            ),
            queueId = "example_queue"
        ),
        DomainEvent.DeleteMessageDomainEvent(
            id = 2,
            messageId = 1,
            queueId = "example_queue"
        ),
        DomainEvent.UpdateMessageDomainEvent(
            id = 3,
            messageId = 1,
            content = "Updated content",
            queueId = "example_queue"
        ),
        DomainEvent.RealmDomainEvent(
            id = 4,
            op = DomainEvent.RealmDomainEvent.OperationType.UPDATE,
            queueId = "example_queue"
        ),
        DomainEvent.HeartbeatDomainEvent(
            id = 5,
            queueId = "example_queue"
        ),
        DomainEvent.UserSubscriptionDomainEvent(
            id = 6,
            queueId = "example_queue",
            op = DomainEvent.UserSubscriptionDomainEvent.OperationType.ADD,
            subscriptions = emptyList()
        ),
        DomainEvent.PresenceDomainEvent(
            id = 7,
            queueId = "example_queue",
            presence = EventPresence.ONLINE,
            userId = 123,
            email = "john@example.com",
            currentUser = true
        ),
        DomainEvent.UserStatusDomainEvent(
            id = 8,
            queueId = "example_queue",
            emojiCode = "emoji_code",
            emojiName = "smile",
            reactionType = "like",
            statusText = "Feeling happy",
            userId = 123
        ),
        DomainEvent.StreamDomainEvent(
            id = 9,
            queueId = "example_queue",
            op = DomainEvent.StreamDomainEvent.OperationType.CREATE,
            streams = emptyList(),
            streamName = "new_stream",
            streamId = 123
        ),
        DomainEvent.ReactionDomainEvent(
            id = 10,
            queueId = "example_queue",
            op = DomainEvent.ReactionDomainEvent.OperationType.ADD,
            emojiCode = "emoji_code",
            emojiName = "smile",
            messageId = 1,
            reactionType = "like",
            userId = 123,
            currentUserReaction = true
        ),
        DomainEvent.ReactionDomainEvent(
            id = 11,
            queueId = "example_queue",
            op = DomainEvent.ReactionDomainEvent.OperationType.REMOVE,
            emojiCode = "emoji_code",
            emojiName = "smile",
            messageId = 1,
            reactionType = "like",
            userId = 123,
            currentUserReaction = true
        ),
        DomainEvent.TypingDomainEvent(
            id = 12,
            queueId = "example_queue",
            op = DomainEvent.TypingDomainEvent.OperationType.START,
            messageType = "stream",
            streamId = 1,
            topic = "topic_name",
            userId = 123,
            userEmail = "john@example.com"
        ),
        DomainEvent.AddMessageFlagEvent(
            id = 13,
            queueId = "example_queue",
            flag = "important",
            addFlagMessagesIds = listOf(1, 2, 3)
        ),
        DomainEvent.RemoveMessageFlagEvent(
            id = 14,
            queueId = "example_queue",
            flag = "important",
            removeFlagMessagesIds = listOf(1, 2, 3)
        ),
        DomainEvent.AddReadMessageFlagEvent(
            id = 15,
            queueId = "example_queue",
            addFlagMessagesIds = listOf(1, 2, 3)
        ),
        DomainEvent.RemoveReadMessageFlagEvent(
            id = 16,
            queueId = "example_queue",
            removeFlagMessagesIds = listOf(1, 2, 3),
            removeFlagMessages = emptyList()
        ),
    )

    val elmServerEvents = listOf(
        ChatEventElm.Internal.ServerEvent.EventQueueFailed(
            queueId = null,
            eventId = -1,
            recreateQueue = true
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueRegistered(
            queueId = "new_queue",
            eventId = 0,
            timeoutSeconds = 60
        ),
        ChatEventElm.Internal.ServerEvent.NewMessage(
            queueId = "example_queue",
            eventId = 1,
            message = ChatMessage(
                id = 1,
                content = "Example content",
                sentAt = sampleZonedDateTime,
                senderId = 123,
                senderName = "John Doe",
                senderAvatarUrl = "http://example.com/1.png",
                reactions = listOf(ChatReaction(sampleEmoji.name, sampleEmoji.code, 1, false)),
                owner = false,
            )
        ),
        ChatEventElm.Internal.ServerEvent.MessageDeleted(
            eventId = 2,
            queueId = "example_queue",
            messageId = 1
        ),
        ChatEventElm.Internal.ServerEvent.MessageUpdated(
            queueId = "example_queue",
            eventId = 3,
            messageId = 1,
            newContent = "Updated content"
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 4
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 5
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 6
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 7
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 8
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 9
        ),
        ChatEventElm.Internal.ServerEvent.ReactionAdded(
            queueId = "example_queue",
            eventId = 10,
            emoji = ChatEmoji(name = "smile", code = "emoji_code"),
            messageId = 1,
            currentUserReaction = true,
        ),
        ChatEventElm.Internal.ServerEvent.ReactionRemoved(
            queueId = "example_queue",
            eventId = 11,
            emoji = ChatEmoji(name = "smile", code = "emoji_code"),
            messageId = 1,
            currentUserReaction = true,
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 12
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 13
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 14
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 15
        ),
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(
            queueId = "example_queue",
            eventId = 16
        )
    )

}
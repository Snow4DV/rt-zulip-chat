package ru.snowadv.chat_presentation.chat.presentation.elm.data

import android.net.http.HttpException
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.chat_domain_api.model.ChatReaction
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.presentation.elm.util.EmojiUtils.generateReactions
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.ScreenState
import java.io.IOException
import java.time.ZonedDateTime

internal class ChatReducerElmTestData {
    val streamName = "TestStream"
    val streamId = 1L
    val topicName = "TestTopic"

    val allMessagesCount = 100
    val pagesCount = 5

    val reactions =
        listOf("happy" to "1f600", "joy" to "1f602", "blush" to "1f60a", "neutral" to "1f610")
    val maxReactionsCount = reactions.size

    val initialResumedState =
        ChatStateElm(
            stream = streamName,
            topic = topicName,
            eventQueueData = null,
            resumed = true,
            sendTopic = topicName,
            streamId = streamId,
        )

    val internetErrorException = HttpException("HTTP 500", IOException())

    val allMessages = List(allMessagesCount) {
        ChatMessage(
            id = it.toLong(),
            content = "Message no $it",
            sentAt = ZonedDateTime.now().plusDays(it.toLong()),
            senderId = it.toLong(),
            senderName = "User $it",
            senderAvatarUrl = "http://img.nonexistant/$it",
            reactions = generateReactions(it, maxReactionsCount, reactions),
            owner = it % 2 == 0,
            topic = "topic",
            isRead = it % 5 != 0,
        )
    }

    val chunkedMessages = allMessages.chunked(allMessagesCount / pagesCount)

    val paginatedMessages = chunkedMessages.mapIndexed { index, chatMessages ->
        ChatPaginatedMessages(
            messages = chatMessages,
            foundAnchor = index == 0,
            foundNewest = index == 0,
            foundOldest = index == pagesCount - 1,
        )
    }

    val emptyPaginatedMessages = ChatPaginatedMessages(
        messages = emptyList(),
        foundAnchor = false,
        foundOldest = false,
        foundNewest = false,
    )

    val sampleUserId = 1L
    val sampleMessageId = 1L
    val sampleText = "Test text"
    val sampleEmojiName = "happy"

    val firstPageStateNullEventQueueData = ChatStateElm(
        stream = streamName,
        topic = topicName,
        screenState = ScreenState.Success(chunkedMessages[0]),
        messages = chunkedMessages[0],
        eventQueueData = null,
        paginationStatus = ChatPaginationStatus.HasMore,
        resumed = true,
        streamId = streamId,
        isTopicEmptyErrorVisible = false,
        isTopicChooserVisible = false,
        sendTopic = topicName,
    )

    val initialEventQueuePropsAfterRegister = EventQueueProperties(
        queueId = "0",
        timeoutSeconds = 10_000,
        lastEventId = -1
    )

    val firstPageMessages = chunkedMessages[0]

    val firstPageStateObserveStarted = ChatStateElm(
        stream = streamName,
        topic = topicName,
        screenState = ScreenState.Success(firstPageMessages),
        messages = firstPageMessages,
        eventQueueData = initialEventQueuePropsAfterRegister,
        paginationStatus = ChatPaginationStatus.HasMore,
        resumed = true,
        isTopicEmptyErrorVisible = false,
        isTopicChooserVisible = false,
        sendTopic = topicName,
        streamId = streamId,
    )


    val sampleMessage = firstPageMessages[0]
    val sampleNewMessage = allMessages.last().let { lastMessage ->
        lastMessage.copy(
            id = lastMessage.id + 1,
            content = "hello!",
            isRead = false,
        )
    }

    val newSubject = "newSubject"

    val queueId = "0"

    val lastMessageInFirstPage = firstPageMessages.last()

    val sampleEmoji = ChatEmoji(sampleEmojiName, "1f600")
    val otherSampleText = "Test2"

    val testServerEvents = listOf(
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(queueId = queueId, eventId = 0),
        ChatEventElm.Internal.ServerEvent.NewMessage(
            queueId = queueId,
            eventId = 0,
            message = chunkedMessages[0][0]
        ),
        ChatEventElm.Internal.ServerEvent.MessageDeleted(
            queueId = queueId,
            eventId = 0,
            messageId = 0
        ),
        ChatEventElm.Internal.ServerEvent.MessageUpdated(
            queueId = queueId,
            eventId = 0,
            messageId = 0,
            newContent = otherSampleText,
            newSubject = newSubject
        ),
        ChatEventElm.Internal.ServerEvent.ReactionAdded(
            queueId = queueId,
            eventId = 0,
            messageId = 0,
            emoji = sampleEmoji,
            currentUserReaction = true
        ),
        ChatEventElm.Internal.ServerEvent.ReactionRemoved(
            queueId = queueId,
            eventId = 0,
            messageId = 0,
            emoji = sampleEmoji,
            currentUserReaction = true
        ),
        ChatEventElm.Internal.ServerEvent.MessagesRead(
            queueId = queueId,
            eventId = 0,
            addFlagMessagesIds = listOf(1,2),
        ),
        ChatEventElm.Internal.ServerEvent.MessagesUnread(
            queueId = queueId,
            eventId = 0,
            removeFlagMessagesIds = listOf(1,2),
        ),
    )
}
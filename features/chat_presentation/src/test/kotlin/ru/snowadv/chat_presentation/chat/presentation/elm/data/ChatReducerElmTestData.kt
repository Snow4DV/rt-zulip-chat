package ru.snowadv.chat_presentation.chat.presentation.elm.data

import android.net.http.HttpException
import ru.snowadv.chat_domain_api.model.ChatEmoji
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.chat_domain_api.model.ChatReaction
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.ScreenState
import java.io.IOException
import java.time.ZonedDateTime

internal class ChatReducerElmTestData {
    val streamName = "TestStream"
    val topicName = "TestTopic"

    val allMessagesCount = 100
    val pagesCount = 5

    val reactions =
        listOf("happy" to "1f600", "joy" to "1f602", "blush" to "1f60a", "neutral" to "1f610")
    val maxReactionsCount = reactions.size

    val initialResumedState =
        ChatStateElm(stream = streamName, topic = topicName, eventQueueData = null, resumed = true)

    val internetErrorException = HttpException("HTTP 500", IOException())

    val allMessages = List(allMessagesCount) {
        ChatMessage(
            id = it.toLong(),
            content = "Message no $it",
            sentAt = ZonedDateTime.now().plusDays(it.toLong()),
            senderId = it.toLong(),
            senderName = "User $it",
            senderAvatarUrl = "http://img.nonexistant/$it",
            reactions = generateReactions(it),
            owner = it % 2 == 0,
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
    )



    val sampleMessage = firstPageMessages[0]
    val sampleNewMessage = allMessages.last().let { lastMessage ->
        lastMessage.copy(
            id = lastMessage.id + 1,
            content = "hello!"
        )
    }

    val lastMessageInFirstPage = firstPageMessages.last()

    val sampleEmoji = ChatEmoji(sampleEmojiName, "1f600")
    val otherSampleText = "Test2"

    val testServerEvents = listOf(
        ChatEventElm.Internal.ServerEvent.EventQueueUpdated(queueId = "0", eventId = 0),
        ChatEventElm.Internal.ServerEvent.NewMessage(queueId = "0", eventId = 0, message = chunkedMessages[0][0]),
        ChatEventElm.Internal.ServerEvent.MessageDeleted(queueId = "0", eventId = 0, messageId = 0),
        ChatEventElm.Internal.ServerEvent.MessageUpdated(queueId = "0", eventId = 0, messageId = 0, newContent = otherSampleText),
        ChatEventElm.Internal.ServerEvent.ReactionAdded(queueId = "0", eventId = 0, messageId = 0, emoji = sampleEmoji, currentUserReaction = true),
        ChatEventElm.Internal.ServerEvent.ReactionRemoved(queueId = "0", eventId = 0, messageId = 0, emoji = sampleEmoji, currentUserReaction = true),
    )

    private fun generateReactions(count: Int): List<ChatReaction> {
        return List(count % maxReactionsCount) {
            ChatReaction(reactions[it].first, reactions[it].second, it + 2, false)
        }
    }
}
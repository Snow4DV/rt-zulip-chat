package ru.snowadv.chat_presentation.chat.presentation.elm.data

import ru.snowadv.chat_domain_api.model.ChatPaginatedMessages
import ru.snowadv.chat_domain_api.model.ChatPaginationStatus
import ru.snowadv.chat_presentation.chat.ui.model.ChatPaginationStatus as UiChatPaginationStatus
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.presentation.elm.util.EmojiUtils.generateReactions
import ru.snowadv.chat_presentation.chat.ui.elm.ChatStateUiElm
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessage as UiChatMessage
import ru.snowadv.chat_domain_api.model.ChatMessage
import ru.snowadv.chat_presentation.chat.presentation.elm.util.EmojiUtils.generateSingleTypeReactions
import ru.snowadv.chat_presentation.chat.presentation.elm.util.EmojiUtils.generateSingleTypeUiReactions
import ru.snowadv.chat_presentation.chat.presentation.elm.util.EmojiUtils.generateUiReactions
import ru.snowadv.chat_presentation.chat.ui.model.ChatDate
import ru.snowadv.chat_presentation.chat.ui.model.ChatMessageType
import ru.snowadv.events_api.model.EventQueueProperties
import ru.snowadv.model.Resource
import ru.snowadv.model.ScreenState
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

internal class ChatElmUiMapperTestData {
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

    val initialResumedStateMapped =
        ChatStateUiElm(
            stream = streamName,
            topic = topicName,
            eventQueueData = null,
            resumed = true,
            sendTopic = topicName,
            screenState = ScreenState.Loading(),
            uploadingFile = false,
            changingReaction = false,
            messages = emptyList(),
            topics = Resource.Loading(),
            isLoading = false,
            paginationStatus = UiChatPaginationStatus.None,
            isTopicChooserVisible = false,
            isTopicEmptyErrorVisible = false,
        )

    val currentZoneDateTiem = ZonedDateTime.of(
        1970, 1, 1, 1, 1, 1, 1, ZoneId.systemDefault(),
    )

    val localDateTime = LocalDateTime.of(1970, 1, 1, 1, 1, 1, 1)

    val allMessages = List(allMessagesCount) {
        ChatMessage(
            id = it.toLong(),
            content = "Message no $it",
            sentAt = currentZoneDateTiem,
            senderId = it.toLong(),
            senderName = "User $it",
            senderAvatarUrl = "http://img.nonexistant/$it",
            reactions = generateSingleTypeReactions(it),
            owner = it % 2 == 0,
            topic = "topic",
            isRead = it % 5 != 0,
        )
    }

    val allUiMessages = List(allMessagesCount) {
        UiChatMessage(
            id = it.toLong(),
            text = "Message no $it",
            sentAt = localDateTime,
            senderId = it.toLong(),
            senderName = "User $it",
            senderAvatarUrl = "http://img.nonexistant/$it",
            reactions = generateSingleTypeUiReactions(it),
            topic = "topic",
            isRead = it % 5 != 0,
            messageType = if (it % 2 == 0) ChatMessageType.OUTGOING else ChatMessageType.INCOMING,
        )
    }

    val chunkedMessages = allMessages.chunked(allMessagesCount / pagesCount)
    val chunkedUiMessages = allUiMessages.chunked(allMessagesCount / pagesCount)

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
        actionButtonType = ChatStateElm.ActionButtonType.SEND_MESSAGE,
        sendTopic = topicName,
        messageField = sampleText,
    )

    val firstPageStateNullEventQueueDataMapped = ChatStateUiElm(
        stream = streamName,
        topic = topicName,
        screenState = ScreenState.Success(listOf(ChatDate(dateTime = localDateTime)) + chunkedUiMessages[0]),
        messages = chunkedUiMessages[0],
        eventQueueData = null,
        paginationStatus = UiChatPaginationStatus.HasMore,
        resumed = true,
        isTopicEmptyErrorVisible = false,
        isTopicChooserVisible = false,
        sendTopic = topicName,
        messageField = sampleText,
        actionButtonType = ChatStateUiElm.ActionButtonType.SEND_MESSAGE,
        isLoading = false,
    )

    val initialEventQueuePropsAfterRegister = EventQueueProperties(
        queueId = "0",
        timeoutSeconds = 10_000,
        lastEventId = -1
    )

    val firstPageMessages = chunkedMessages[0]
    val firstPageUiMessages = chunkedUiMessages[0]

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

    val firstPageStateObserveStartedMapped = ChatStateUiElm(
        stream = streamName,
        topic = topicName,
        screenState = ScreenState.Success(listOf(ChatDate(dateTime = localDateTime)) + chunkedUiMessages[0]),
        messages = firstPageUiMessages,
        eventQueueData = initialEventQueuePropsAfterRegister,
        paginationStatus = UiChatPaginationStatus.HasMore,
        resumed = true,
        isTopicEmptyErrorVisible = false,
        isTopicChooserVisible = false,
        sendTopic = topicName,
        isLoading = false,
    )
}
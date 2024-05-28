package ru.snowadv.chatapp.server

import com.github.tomakehurst.wiremock.common.FileSource
import com.github.tomakehurst.wiremock.extension.Parameters
import com.github.tomakehurst.wiremock.extension.ResponseTransformer
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import com.github.tomakehurst.wiremock.http.Request
import com.github.tomakehurst.wiremock.http.RequestMethod
import com.github.tomakehurst.wiremock.http.Response
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.chatapp.data.EmojiData
import ru.snowadv.chatapp.data.MockData
import ru.snowadv.chatapp.model.ErrorResponseDto
import ru.snowadv.chatapp.server.queue.MockEventQueueData
import ru.snowadv.network.model.EventQueueResponseDto
import ru.snowadv.network.model.EventResponseDto
import ru.snowadv.network.model.EventsResponseDto
import ru.snowadv.network.model.MessageResponseDto
import ru.snowadv.network.model.ReactionResponseDto
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

internal class ChatServerResponseTransformer @Inject constructor(
    private val data: MockData,
    private val emojiData: EmojiData,
) :
    ResponseTransformer() {
    companion object {
        const val NAME = "chat_server_response_transformer"
        const val STREAM_NAME = "general"
        const val TOPIC_NAME = "testing"
        const val STREAM_ID = 1L
        val totalRegex by lazy { ".*/api/v1/(messages|register|events).*".toRegex() }
        private val headerContentType by lazy { HttpHeader("Content-Type", "application/json") }
    }


    private val json by lazy { Json { ignoreUnknownKeys = true } }
    private var chatMessages = data.messagesDto
    private val queues = ConcurrentHashMap<String, MockEventQueueData>() // queue id to queue data
    private val lastQueueId = AtomicInteger(0)

    override fun getName(): String {
        return NAME
    }

    override fun transform(
        request: Request?,
        response: Response?,
        files: FileSource?,
        parameters: Parameters?
    ): Response {
        return Response.Builder.like(response)
            .headers((response?.headers ?: HttpHeaders()).plus(headerContentType))
            .apply { request?.let { processRequest(request) } ?: run { error("Request is null!") } }
            .build()
    }

    @Synchronized
    private fun Response.Builder.processRequest(request: Request): Response.Builder {
        return when {
            request.url.matches("/api/v1/messages.*".toRegex()) && request.method == RequestMethod.GET -> {
                body(json.encodeToString(chatMessages)).status(200)
            }

            request.url.matches("/api/v1/register.*".toRegex()) && request.method == RequestMethod.POST -> {
                body(json.encodeToString(registerNewQueue())).status(200)
            }

            request.url.matches("/api/v1/events.*".toRegex()) && request.method == RequestMethod.GET -> {
                getEvents(
                    queueId = request.queryParameter("queue_id").values().first()
                )?.let { events ->
                    body(json.encodeToString(EventsResponseDto(events))).status(200)
                } ?: run {
                    status(400).body(json.encodeToString(getBadRequestError("Wrong queue id")))
                }
            }

            request.url.matches("/api/v1/messages/[0-9]+/reactions.*".toRegex()) && request.method == RequestMethod.POST -> {

                val result = addReactionToMessage(
                    reactionName = request.getBodyParamOrThrow("emoji_name"),
                    messageId = request.getUrlParamByRegexFirstGroup("/api/v1/messages/([0-9]+)/reactions.*".toRegex())
                        .toLong(),
                    userId = data.user.id,
                )
                if (result) {
                    status(200)
                } else {
                    status(400).body(json.encodeToString(getBadRequestError("Reaction already exists.")))
                }
            }

            request.url.matches("/api/v1/messages/[0-9]+/reactions.*".toRegex()) && request.method == RequestMethod.DELETE -> {

                val result = removeReactionFromMessage(
                    reactionName = request.getBodyParamOrThrow("emoji_name"),
                    messageId = request.getUrlParamByRegexFirstGroup("/api/v1/messages/([0-9]+)/reactions.*".toRegex())
                        .toLong(),
                    userId = data.user.id,
                )
                if (result) {
                    status(200)
                } else {
                    status(400).body(json.encodeToString(getBadRequestError("Reaction doesn't exist.")))
                }
            }

            request.url.matches("/api/v1/messages.*".toRegex()) && request.method == RequestMethod.POST -> {
                sendMessage(request.getQueryParamOrThrow("content"))
                status(200)
            }

            else -> this
        }
    }

    private fun getEvents(delay: Long = 1000, queueId: String): List<EventResponseDto>? {
        Thread.sleep(delay)
        val queue = queues[queueId] ?: return null
        return if (queue.hasNewEvent()) {
            queues[queueId] = queue.markEventsAsViewed()
            queue.events.filterIndexed { index, _ ->
                index in (queue.lastCollectedId + 1 until queue.events.size)
            }
        } else {
            listOf(getHeartbeatEventAndAddToQueue(queueId))
        }
    }

    private fun sendMessage(content: String) {
        val newMessage = getNewMessage(content, chatMessages.messages.last().id + 1)
        chatMessages = chatMessages.copy(
            messages = chatMessages.messages + newMessage,
        )
        addNewMessageToEventQueues(newMessage)
    }

    private fun addReactionToMessage(reactionName: String, messageId: Long, userId: Long): Boolean {
        chatMessages = chatMessages.copy(
            messages = chatMessages.messages.map { messageDto ->
                if (messageDto.id == messageId) {
                    if (messageDto.reactions.any { it.userId == userId && it.emojiName == reactionName }) return false
                    val newReaction = createReactionWithNameByUser(reactionName, userId)
                    addReactionChangedToEventQueues(
                        reaction = newReaction,
                        messageId = messageId,
                        userId = userId,
                        op = EventResponseDto.ReactionEventDto.OperationType.ADD,
                    )
                    messageDto.copy(reactions = messageDto.reactions + newReaction)
                } else {
                    messageDto
                }
            }
        )
        return true
    }

    private fun removeReactionFromMessage(
        reactionName: String,
        messageId: Long,
        userId: Long
    ): Boolean {
        chatMessages = chatMessages.copy(
            messages = chatMessages.messages.map { messageDto ->
                if (messageDto.id == messageId) {
                    val reaction =
                        messageDto.reactions.firstOrNull { it.userId == userId && it.emojiName == reactionName }
                            ?: return false
                    addReactionChangedToEventQueues(
                        reaction = reaction,
                        messageId = messageId,
                        userId = userId,
                        op = EventResponseDto.ReactionEventDto.OperationType.REMOVE,
                    )
                    messageDto.copy(reactions = messageDto.reactions - reaction)
                } else {
                    messageDto
                }
            }
        )
        return true
    }

    private fun getHeartbeatEventAndAddToQueue(queueId: String): EventResponseDto {
        queues[queueId]?.let { queue ->
            val event = EventResponseDto.HeartbeatEventDto(
                id = queue.lastCollectedId + 1,
            )
            queues[queue.queueId] = queue.copy(
                events = queue.events + event,
                lastCollectedId = event.id,
            )
            return event
        } ?: error("queue not found")
    }

    private fun addNewMessageToEventQueues(message: MessageResponseDto) {
        queues.entries.forEach {
            queues[it.key] = it.value.copy(
                events = it.value.events + getNewEventForMessage(
                    message = message,
                    eventId = it.value.events.getIdForNewEvent(),
                )
            )
        }
    }

    private fun addReactionChangedToEventQueues(
        reaction: ReactionResponseDto,
        messageId: Long,
        userId: Long,
        op: EventResponseDto.ReactionEventDto.OperationType,
    ) {
        queues.entries.forEach {
            queues[it.key] = it.value.copy(
                events = it.value.events + getNewEventForReactionChanged(
                    newEventId = it.value.events.getIdForNewEvent(),
                    reaction = reaction,
                    messageId = messageId,
                    userId = userId,
                    op = op,
                )
            )
        }
    }


    private fun List<EventResponseDto>.getIdForNewEvent(): Long {
        return (lastOrNull()?.id ?: -1L) + 1
    }

    private fun Request.getQueryParamOrThrow(name: String): String {
        return queryParameter(name).values().first()
    }

    private fun Request.getBodyParamOrThrow(name: String): String {
        val queryParamRegex = "$name=([^\\r\\n\\t ]+)".toRegex()
        return queryParamRegex.find(bodyAsString)?.groups?.get(1)?.value
            ?: error("Query param $name not found")
    }

    private fun Request.getUrlParamByRegexFirstGroup(regex: Regex): String {
        return regex.find(url)?.groups?.get(1)?.value
            ?: throw NoSuchElementException("Group that matches regex $regex wasn't found")
    }


    private fun getNewEventForMessage(
        message: MessageResponseDto,
        eventId: Long
    ): EventResponseDto.MessageEventDto {
        return EventResponseDto.MessageEventDto(
            id = eventId,
            message = message,
            flags = emptyList(),
        )
    }

    private fun getNewEventForReactionChanged(
        reaction: ReactionResponseDto,
        messageId: Long,
        userId: Long,
        newEventId: Long,
        op: EventResponseDto.ReactionEventDto.OperationType,
    ): EventResponseDto.ReactionEventDto {
        return EventResponseDto.ReactionEventDto(
            id = newEventId,
            op = op,
            emojiCode = reaction.emojiCode,
            emojiName = reaction.emojiName,
            messageId = messageId,
            reactionType = reaction.reactionType,
            userId = userId,
        )
    }

    private fun getNewMessage(content: String, messageId: Long): MessageResponseDto {
        return MessageResponseDto(
            id = messageId,
            content = content,
            timestamp = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC),
            senderId = data.user.id,
            senderFullName = data.userName,
            avatarUrl = null,
            reactions = emptyList(),
            type = "stream",
            subject = TOPIC_NAME,
            streamId = STREAM_ID,
            flags = emptyList(),
        )
    }

    private fun registerNewQueue(): EventQueueResponseDto {
        val queueId = lastQueueId.getAndIncrement().toString()
        queues[queueId] = MockEventQueueData(
            queueId = queueId,
        )
        return EventQueueResponseDto(
            queueId = queueId,
            lastEventId = -1,
            longPollTimeoutSeconds = 10_000,
        )
    }

    private fun createReactionWithNameByUser(name: String, userId: Long): ReactionResponseDto {
        return ReactionResponseDto(
            userId = data.user.id,
            emojiName = name,
            emojiCode = emojiData.emojisByName[name]?.code
                ?: emojiData.emojisByName.values.random().code,
            reactionType = "unicode_emoji",
        )
    }

    private fun getBadRequestError(message: String): ErrorResponseDto {
        return ErrorResponseDto(msg = message)
    }
}
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.snowadv.chatapp.auth_mock.DummyAuth
import ru.snowadv.chatapp.server.queue.MockEventQueueData
import ru.snowadv.chatapp.util.AssetsUtils.fromAssets
import ru.snowadv.network.model.EventQueueResponseDto
import ru.snowadv.network.model.EventResponseDto
import ru.snowadv.network.model.EventsResponseDto
import ru.snowadv.network.model.MessageResponseDto
import ru.snowadv.network.model.MessagesResponseDto
import ru.snowadv.network.model.ReactionResponseDto
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

internal class ChatServerResponseTransformer : ResponseTransformer() {
    
    private val json = Json { ignoreUnknownKeys = true }
    companion object {
        const val NAME = "chat_server_response_transformer"
        const val STREAM_NAME = "stream"
        const val TOPIC_NAME = "topic"
        const val STREAM_ID = 1L
        val currentUserId = DummyAuth.user.id
        const val CURRENT_USER_NAME = "User 1"
        val addReaction =
            ReactionResponseDto(currentUserId, "stuck_out_tongue", "1f61b", "unicode_emoji")
    }


    private val chatMessages = MutableStateFlow(getInitMessages())
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
            .status(200)
            .headers(
                (response?.headers ?: HttpHeaders()).plus(
                    HttpHeader("Content-Type", "application/json")
                )
            )
            .apply { if (request != null) processRequest(request) else error("Request is null!") }
            .build()
    }

    private fun Response.Builder.processRequest(request: Request): Response.Builder {
        return when {
            request.url.contains("/api/v1/messages") && request.method == RequestMethod.GET -> {
                body(json.encodeToString(chatMessages.value))
            }

            request.url.contains("/api/v1/register") && request.method == RequestMethod.POST -> {
                body(json.encodeToString(registerNewQueue()))
            }

            request.url.contains("/api/v1/events") && request.method == RequestMethod.GET -> {
                body(
                    json.encodeToString(
                        EventsResponseDto(
                            events = getEvents(
                                queueId = request.queryParameter("queue_id").values().first()
                            ),
                        )
                    )
                )
            }

            request.url.contains("reactions") && request.method == RequestMethod.POST -> {
                addReactionToLastMessageByCurrentUser()
                this
            }

            request.url.contains("/api/v1/messages") && request.method == RequestMethod.POST -> {
                sendMessage(request.queryParameter("content").values().first())
                this
            }

            request.url.contains("streams") && request.method == RequestMethod.GET -> {
                body(fromAssets("channels/streams.json"))
            }

            request.url.contains("subscriptions") && request.method == RequestMethod.GET -> {
                body(fromAssets("channels/subscriptions.json"))
            }

            request.url.contains("topics") && request.method == RequestMethod.GET -> {
                body(fromAssets("channels/topics.json"))
            }

            request.url.endsWith("users") && request.method == RequestMethod.GET -> {
                body(fromAssets("people/people.json"))
            }

            request.url.contains("realm/presence") && request.method == RequestMethod.GET -> {
                body(fromAssets("people/realm_presence.json"))
            }

            request.url.contains("users/") && request.method == RequestMethod.GET -> {
                body(fromAssets("profile/profile.json"))
            }

            request.url.contains("/presence") && request.method == RequestMethod.GET -> {
                body(fromAssets("profile/presence.json"))
            }

            else -> error("Url  '${request.url}' didn't match")
        }
    }

    private fun getEvents(delay: Long = 500, queueId: String): List<EventResponseDto> =
        runBlocking {
            delay(delay)
            val queue = queues[queueId] ?: error("queue not found")
            return@runBlocking if (queue.hasNewEvent()) {
                queue.events.filterIndexed { index, _ ->
                    index in (queue.lastCollectedId + 1 until queue.events.size)
                }
            } else {
                listOf(getHeartbeatEventAndAddToQueue(queueId))
            }
        }

    @Synchronized
    private fun sendMessage(content: String) {
        chatMessages.value.let { messagesDto ->
            val newMessage = getNewMessage(content, messagesDto.messages.last().id + 1)
            chatMessages.tryEmit(
                messagesDto.copy(
                    messages = messagesDto.messages + newMessage,
                )
            )
            addNewMessageToEventQueues(newMessage)
        }
    }

    @Synchronized
    private fun addReactionToLastMessageByCurrentUser() {
        chatMessages.value.let { messagesDto ->
            val lastMessage = messagesDto.messages.last()
            val updatedLastMessage = lastMessage.copy(
                reactions = lastMessage.reactions + addReaction
            )
            chatMessages.tryEmit(
                messagesDto.copy(
                    messages = messagesDto.messages - lastMessage + updatedLastMessage,
                )
            )
            addReactionAddedToEventQueues(addReaction, lastMessage.id, currentUserId)
        }
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

    @Synchronized
    private fun addNewMessageToEventQueues(message: MessageResponseDto) {
        queues.entries.forEach {
            queues[it.key] = it.value.copy(
                events = it.value.events + getNewEventForMessage(
                    message = message,
                    lastEventId = it.value.events.getIdForNewEvent(),
                )
            )
        }
    }

    @Synchronized
    private fun addReactionAddedToEventQueues(
        reaction: ReactionResponseDto,
        messageId: Long,
        userId: Long,
    ) {
        queues.entries.forEach {
            queues[it.key] = it.value.copy(
                events = it.value.events + getNewEventForReactionAdded(
                    lastEventId = it.value.events.getIdForNewEvent(),
                    reaction = reaction,
                    messageId = messageId,
                    userId = userId,
                )
            )
        }
    }


    private fun List<EventResponseDto>.getIdForNewEvent(): Long {
        return (lastOrNull()?.id ?: -1L) + 1
    }


    private fun getNewEventForMessage(
        message: MessageResponseDto,
        lastEventId: Long
    ): EventResponseDto.MessageEventDto {
        return EventResponseDto.MessageEventDto(
            id = lastEventId + 1,
            message = message,
        )
    }

    private fun getNewEventForReactionAdded(
        reaction: ReactionResponseDto,
        messageId: Long,
        userId: Long,
        lastEventId: Long
    ): EventResponseDto.ReactionEventDto {
        return EventResponseDto.ReactionEventDto(
            id = lastEventId + 1,
            op = EventResponseDto.ReactionEventDto.OperationType.ADD,
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
            senderId = currentUserId,
            senderFullName = CURRENT_USER_NAME,
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

    private fun getInitMessages(): MessagesResponseDto {
        return json.decodeFromString(fromAssets("chat/messages.json"))
    }
}
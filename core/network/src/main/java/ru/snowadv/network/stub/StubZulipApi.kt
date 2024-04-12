package ru.snowadv.network.stub

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.http.Query
import ru.snowadv.network.api.ZulipApi
import ru.snowadv.network.model.AllStreamsDto
import ru.snowadv.network.model.AllUsersDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.EmojisDto
import ru.snowadv.network.model.EventDto
import ru.snowadv.network.model.EventQueueDto
import ru.snowadv.network.model.EventsDto
import ru.snowadv.network.model.MessageDto
import ru.snowadv.network.model.MessagesDto
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.PresenceSourcesDto
import ru.snowadv.network.model.ReactionDto
import ru.snowadv.network.model.SingleUserDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.SubscribedStreamsDto
import ru.snowadv.network.model.TopicsDto

object StubZulipApi : ZulipApi {

    private val messages = MutableStateFlow(emptyList<MessageDto>())

    private val eventsFlow = MutableStateFlow(emptyList<EventDto>())

    private var lastEventId = -1L


    override suspend fun getAllStreams(): Result<AllStreamsDto> {
        return resultAfterDelay { Result.success(AllStreamsDto(StubData.streams)) }
    }

    override suspend fun getSubscribedStreams(): Result<SubscribedStreamsDto> {
        return resultAfterDelay { Result.success(SubscribedStreamsDto(StubData.subscribedStreams)) }
    }

    override suspend fun getTopicsByChannel(streamId: Long): Result<TopicsDto> {
        return resultAfterDelay {
            Result.success(
                TopicsDto(
                    StubData.streamIdToTopicDto[streamId] ?: emptyList()
                )
            )
        }
    }

    override suspend fun getAllUsers(): Result<AllUsersDto> {
        return resultAfterDelay { Result.success(AllUsersDto(StubData.people)) }
    }

    override suspend fun getUser(userId: Long): Result<SingleUserDto> {
        return resultAfterDelay {
            StubData.people.firstOrNull { it.id.toLong() == userId }
                ?.let { Result.success(SingleUserDto(it)) }
                ?: Result.failure(NoSuchElementException())
        }
    }

    override suspend fun getCurrentUser(): Result<SingleUserDto> {
        return resultAfterDelay { Result.success(SingleUserDto(StubData.people.first())) }
    }

    override suspend fun getAllUsersPresence(): Result<AllUsersPresenceDto> {
        return resultAfterDelay {
            Result.success(AllUsersPresenceDto(StubData.peoplePresenceDtosByEmail.mapValues {
                PresenceSourcesDto(
                    it.value,
                    it.value
                )
            }, StubData.serverTimestamp.toDouble()))
        }
    }

    override suspend fun getUserPresence(userId: Long): Result<SingleUserPresenceDto> {
        return resultAfterDelay {
            StubData.peoplePresenceDtosById[userId]?.let {
                Result.success(
                    SingleUserPresenceDto(PresenceSourcesDto(it, it))
                )
            } ?: Result.failure(NoSuchElementException())
        }
    }

    override suspend fun getMessages(
        @Query(value = "anchor") anchor: String,
        @Query(value = "num_before") numBefore: Int,
        @Query(value = "num_after") numAfter: Int,
        @Query(value = "narrow") narrow: List<NarrowDto>
    ): Result<MessagesDto> {
        return resultAfterDelay { Result.success(MessagesDto(messages.value)) }
    }

    override suspend fun sendMessage(stream: String, topic: String, text: String): Result<Unit> {
        sendMessageToState(stream, topic, text)
        return resultAfterDelay { Result.success(Unit) }
    }

    override fun editMessage(messageId: Long, content: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override fun deleteMessage(messageId: Long): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun addReaction(messageId: Long, emojiName: String): Result<Unit> {
        addReactionToState(messageId, emojiName)
        return resultAfterDelay { Result.success(Unit) }
    }

    override suspend fun removeReaction(messageId: Long, emojiName: String): Result<Unit> {
        removeReactionFromState(messageId, emojiName)
        return resultAfterDelay { Result.success(Unit) }
    }

    override suspend fun getEmojis(): Result<EmojisDto> {
        return resultAfterDelay { Result.success(EmojisDto(StubData.emojiMap)) }
    }

    override suspend fun registerEventQueue(
        eventTypes: List<String>,
        narrow: List<List<String>>
    ): Result<EventQueueDto> {
        return resultAfterDelay { Result.success(EventQueueDto("1", lastEventId)) }
    }

    override suspend fun getEventsFromEventQueue(
        queueId: String,
        lastEventId: Long
    ): Result<EventsDto> {
        return resultAfterDelay {
            Result.success(
                EventsDto(
                    eventsFlow.map { it.filter { it.id > lastEventId } }.flowOn(Dispatchers.Default)
                        .first { it.isNotEmpty() }
                )
            )
        }
    }

    private suspend fun <T> resultAfterDelay(actionWithResult: suspend () -> Result<T>): Result<T> {
        delay(800)
        return actionWithResult()
    }


    private fun sendMessageToState(
        streamName: String, topicName: String, text: String
    ) {
        val sender = StubData.people.first()

        val lastMessageId = messages.value.lastOrNull()?.id ?: 0
        messages.value += MessageDto(
            id = lastMessageId + 1,
            content = text,
            senderId = sender.id,
            timestamp = StubData.serverTimestamp,
            avatarUrl = sender.avatarUrl,
            senderFullName = sender.name,
            reactions = emptyList(),
        ).also { eventsFlow.value += EventDto.MessageEventDto(++lastEventId, it) }
        addRandomAnswerMessage()

    }

    private fun addReactionToState(messageId: Long, reactionName: String): Boolean {
        messages.value = messages.value.map {
            if (it.id == messageId) {
                addReactionToChatMessage(it, reactionName)?.let {
                    eventsFlow.value += EventDto.MessageEventDto(++lastEventId, it)
                    it
                }
                    ?: return false
            } else {
                it
            }
        }
        return true
    }

    private fun removeReactionFromState(
        messageId: Long, reactionName: String
    ): Boolean {
        messages.value = messages.value.map {
            if (it.id == messageId) {
                removeReactionFromChatMessage(it, reactionName)?.let {
                    eventsFlow.value += EventDto.MessageEventDto(++lastEventId, it)
                    it
                }
                    ?: return false
            } else {
                it
            }
        }
        return true
    }

    private fun addRandomAnswerMessage() {
        val sender = StubData.people.filter { it.id != StubData.people.first().id }.random()

        val lastMessageId = messages.value.lastOrNull()?.id ?: 0
        messages.value += MessageDto(
            id = lastMessageId + 1,
            content = StubData.randomMessages.random(),
            senderId = sender.id,
            timestamp = StubData.serverTimestamp,
            avatarUrl = sender.avatarUrl,
            senderFullName = sender.name,
            reactions = emptyList(),
        ).also { eventsFlow.value += EventDto.MessageEventDto(++lastEventId, it) }
    }

    private fun addReactionToChatMessage(message: MessageDto, emojiName: String): MessageDto? {
        if (message.reactions.any { it.emojiName == emojiName && it.userId == StubData.currentUser.id }) {
            return null // Reaction already exists by current user
        }

        return message.copy(
            reactions = message.reactions + ReactionDto(
                StubData.currentUser.id,
                emojiName,
                StubData.emojiMap[emojiName]!!,
                "unicode_emoji"
            )
        )
    }

    private fun removeReactionFromChatMessage(
        message: MessageDto,
        reactionName: String
    ): MessageDto? {
        val removeReaction =
            message.reactions.firstOrNull {
                it.emojiName == reactionName && it.userId == StubData.currentUser.id
            } ?: return null // Cant remove reaction that doesn't exist

        return message.copy(
            reactions = message.reactions - removeReaction
        )
    }
}
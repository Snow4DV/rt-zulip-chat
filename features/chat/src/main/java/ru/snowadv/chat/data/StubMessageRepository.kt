package ru.snowadv.chat.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.domain.model.Resource
import java.time.ZonedDateTime

internal class StubMessageRepository : MessageRepository {

    private val messages = MutableStateFlow(emptyList<ChatMessage>())
    private val randomMessages = listOf(
        "Hi",
        "Hello",
        "How are you?",
        "Test message",
        "Guys, psst, how much are you getting paid?",
        "Guys i accidentally deleted prod db. Where do i get a backup? Do we really have backups or i should bail out?",
    )

    override fun getMessages(): Flow<Resource<List<ChatMessage>>> {
        return messages
            .onStart {
                Resource.Loading(null) // Simulate connection to the server
                delay(100)
            }
            .map { Resource.Success(it) }
    }

    override fun sendMessage(
        streamName: String,
        topicName: String,
        text: String
    ): Flow<Resource<Unit>> = constructCompletableFlowWithDelay {
        val lastMessageId = messages.value.lastOrNull()?.id ?: 0
        messages.value += ChatMessage(
            id = lastMessageId + 1,
            content = text,
            sentAt = ZonedDateTime.now(),
            senderId = 1,
            senderName = "Ivan Ivanov",
            senderAvatarUrl = null,
            reactions = emptyList()
        )
        addRandomAnswerMessage()
    }

    override fun addReaction(
        messageId: Long,
        reactionName: String
    ): Flow<Resource<Unit>> = constructCompletableFlowWithDelay {


    }

    override fun removeReaction(
        messageId: Long,
        reactionName: String
    ): Flow<Resource<Unit>> = constructCompletableFlowWithDelay {

    }

    private fun constructCompletableFlowWithDelay(action: () -> Unit) = flow {
        emit(Resource.Loading())
        delay(100) // Simulate server delay
        action()
        emit(Resource.Success(Unit))
    }

    private fun addRandomAnswerMessage() {
        val lastMessageId = messages.value.lastOrNull()?.id ?: 0
        messages.value += ChatMessage(
            id = lastMessageId + 1,
            content = randomMessages.random(),
            sentAt = ZonedDateTime.now(),
            senderId = 2,
            senderName = "Petr Petrov",
            senderAvatarUrl = null,
            reactions = emptyList()
        )
    }


}
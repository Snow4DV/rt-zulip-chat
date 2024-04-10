package ru.snowadv.chat.data.repository

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import ru.snowadv.chat.domain.model.ChatMessage
import ru.snowadv.chat.domain.model.ChatReaction
import ru.snowadv.chat.domain.repository.MessageRepository
import ru.snowadv.domain.model.Resource
import java.time.ZonedDateTime

internal object StubMessageRepository : MessageRepository {

    // Like websocket subscription to the chat. Will be replaced with Zulip's events system later.
    private val messages = MutableStateFlow(emptyList<ChatMessage>())
    private val randomMessages = listOf(
        "Hi",
        "Hello",
        "How are you?",
        "Test message",
        "Guys, psst, how much are you getting paid?",
        "Guys i accidentally deleted prod db. Where do i get a backup? Do we really have backups or i should bail out?",
    )

    // Server also knows about emojis. Will be removed when I replace stubs with real repositories
    private val emojiMapByName = emojiMap.values.associateBy { it.name }

    override fun getMessages(streamName: String, topicName: String): Flow<Resource<List<ChatMessage>>> {
        return messages.onStart {
            Resource.Loading // Simulate connection to the server
            delay(200)
        }.map { Resource.Success(it) }
    }

    override fun sendMessage(
        streamName: String, topicName: String, text: String
    ): Flow<Resource<Unit>> = constructCompletableFlowWithDelay {
        val lastMessageId = messages.value.lastOrNull()?.id ?: 0
        messages.value += ChatMessage(
            id = lastMessageId + 1,
            content = text,
            sentAt = ZonedDateTime.now(),
            senderId = 1,
            senderName = "Anastasia Petrova",
            senderAvatarUrl = null,
            reactions = emptyList()
        )
        addRandomAnswerMessage()
        return@constructCompletableFlowWithDelay Resource.Success(Unit)
    }

    override fun addReaction(messageId: Long, reactionName: String): Flow<Resource<Unit>> =
        constructCompletableFlowWithDelay {
            messages.value = messages.value.map {
                if (it.id == messageId) {
                    addReactionToChatMessage(it, reactionName)
                        ?: return@constructCompletableFlowWithDelay Resource.Error()
                } else {
                    it
                }
            }
            return@constructCompletableFlowWithDelay Resource.Success(Unit)
        }

    override fun removeReaction(
        messageId: Long, reactionName: String
    ): Flow<Resource<Unit>> = constructCompletableFlowWithDelay {
        messages.value = messages.value.map {
            if (it.id == messageId) {
                removeReactionFromChatMessage(it, reactionName)
                    ?: return@constructCompletableFlowWithDelay Resource.Error()
            } else {
                it
            }
        }
        return@constructCompletableFlowWithDelay Resource.Success(Unit)
    }

    private fun addRandomAnswerMessage() {
        val lastMessageId = messages.value.lastOrNull()?.id ?: 0
        messages.value += ChatMessage(
            id = lastMessageId + 1,
            content = randomMessages.random(),
            sentAt = ZonedDateTime.now(),
            senderId = 2,
            senderName = "Anastasia Petrova",
            senderAvatarUrl = null,
            reactions = emptyList()
        )
    }

    private fun addReactionToChatMessage(message: ChatMessage, reactionName: String): ChatMessage? {
        val reaction = message.reactions.firstOrNull { it.name == reactionName }

        reaction?.let { it ->
            return message.copy(
                reactions = message.reactions.map {
                    if (it.name == reactionName && !it.userReacted) {
                        it.copy(
                            count = it.count + 1, userReacted = true
                        )
                    } else if (it.name == reactionName){
                        return null // Fail to add reaction as it already exists
                    } else {
                        it
                    }
                }.sortByCountThenCode()
            )
        } ?: run {
            return message.copy(
                reactions = (message.reactions + ChatReaction(
                    name = reactionName,
                    code = emojiMapByName[reactionName]?.code
                        ?: error("No reaction with name $reactionName"),
                    count = 1,
                    userReacted = true,
                )).sortByCountThenCode()
            )
        }
    }

    private fun removeReactionFromChatMessage(message: ChatMessage, reactionName: String): ChatMessage? {
        if (message.reactions.indexOfFirst { it.name == reactionName } == -1) {
            return null // Cant remove reaction that doesn't exist
        }
        return message.copy(
            reactions = message.reactions.mapNotNull {
                if (it.name  == reactionName && it.count  <= 1) { // Delete reaction
                    null
                } else if(it.name == reactionName) ( // Update reaction
                    it.copy(
                        count = it.count - 1
                    )
                ) else { // Keep other reactions as is
                    it
                }
            }
        )
    }


    private fun List<ChatReaction>.sortByCountThenCode(): List<ChatReaction> {
        return this.sortedWith(compareBy({ it.count }, { it.code }))
    }

    private fun constructCompletableFlowWithDelay(action: () -> Resource<Unit>) = flow {
        emit(Resource.Loading)
        delay(100) // Simulate server delay
        emit(action())
    }


}
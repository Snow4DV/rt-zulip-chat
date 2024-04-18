package ru.snowadv.network.api

import ru.snowadv.network.model.AllStreamsDto
import ru.snowadv.network.model.AllUsersDto
import ru.snowadv.network.model.AllUsersPresenceDto
import ru.snowadv.network.model.EmojisDto
import ru.snowadv.network.model.EventQueueDto
import ru.snowadv.network.model.EventsDto
import ru.snowadv.network.model.MessagesDto
import ru.snowadv.network.model.NarrowDto
import ru.snowadv.network.model.SingleUserDto
import ru.snowadv.network.model.SingleUserPresenceDto
import ru.snowadv.network.model.SubscribedStreamsDto
import ru.snowadv.network.model.TopicsDto

interface ZulipApi { // Will add annotations later, at this moment it is used with stubs only
    // Streams
    suspend fun getAllStreams(): Result<AllStreamsDto>
    suspend fun getSubscribedStreams(): Result<SubscribedStreamsDto>

    // Topics
    suspend fun getTopicsByChannel(streamId: Long): Result<TopicsDto>

    // Users
    suspend fun getAllUsers(): Result<AllUsersDto>
    suspend fun getUser(userId: Long): Result<SingleUserDto>
    suspend fun getCurrentUser(): Result<SingleUserDto>

    // Users presence
    suspend fun getAllUsersPresence(): Result<AllUsersPresenceDto>
    suspend fun getUserPresence(userId: Long): Result<SingleUserPresenceDto>

    // Messages
    suspend fun getMessages(narrow: List<NarrowDto>): Result<MessagesDto> // TODO: add pagination
    suspend fun sendMessage(stream: String, topic: String, text: String): Result<Unit>
    suspend fun addReaction(messageId: Long, emojiName: String): Result<Unit>
    suspend fun removeReaction(messageId: Long, emojiName: String): Result<Unit>

    // Emojis
    suspend fun getEmojis(): Result<EmojisDto>

    // Event API
    /**
     * Event types: message (messages), subscription (changes in your subscriptions),
     * realm_user (changes to users in the organization and their properties, such as their name)
     *
     * Narrow example: "narrow": [["is", "dm"], ["stream", "Denmark"]]
     *
     * see https://zulip.com/api/construct-narrow
     */
    suspend fun registerEventQueue(
        eventTypes: List<String>,
        narrow: List<List<String>>,
    ): Result<EventQueueDto>
    suspend fun getEventsFromEventQueue(
        queueId: String,
        lastEventId: Long,
    ): Result<EventsDto>
}
package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.snowadv.network.serializer.MessageDetailsResponseDtoMapSerializer

@Serializable
data class EventsResponseDto(
    @SerialName("events")
    val events: List<EventResponseDto>
)

@Serializable
data class EventQueueResponseDto(
    @SerialName("queue_id")
    val queueId: String,
    @SerialName("last_event_id")
    val lastEventId: Long,
    @SerialName("event_queue_longpoll_timeout_seconds")
    val longPollTimeoutSeconds: Int,
    @SerialName("unread_msgs")
    val unreadMessages: UnreadMessagesRseponseDto? = null,
)

@Serializable
data class UnreadMessagesRseponseDto(
    @SerialName("streams")
    val streams: List<StreamTopicUnreadMessagesResponseDto>
)

@Serializable
data class StreamTopicUnreadMessagesResponseDto(
    @SerialName("stream_id")
    val streamId: Long,
    @SerialName("topic")
    val topicName: String,
    @SerialName("unread_message_ids")
    val unreadMessagesIds: List<Long>,
)

@Serializable
sealed class EventResponseDto {
    abstract val id: Long
    @Serializable
    @SerialName(MESSAGE_EVENT_TYPE)
    data class MessageEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("message")
        val message: MessageResponseDto,
        @SerialName("flags")
        val flags: List<String>,
    ): EventResponseDto()
    @Serializable
    @SerialName(DELETE_MESSAGE_EVENT_TYPE)
    data class DeleteMessageEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("message_id")
        val messageId: Long,
        @SerialName("stream_id")
        val streamId: Long? = null, // can be absent if type is "private"
        @SerialName("topic")
        val topic: String? = null, // can be absent if type is "private"
        @SerialName("message_type")
        val messageType: MessageType, // "stream" or "private"
    ): EventResponseDto() {
        @Serializable
        enum class MessageType(val value: String) {
            @SerialName("stream")
            STREAM("stream"),
            @SerialName("private")
            PRIVATE("private"),
        }
    }

    @Serializable
    @SerialName(UPDATE_MESSAGE_EVENT_TYPE)
    data class UpdateMessageEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("message_id")
        val messageId: Long,
        @SerialName("rendered_content")
        val content: String? = null, // Will only present if message content has changed.
        @SerialName("subject")
        val subject: String? = null, // Will only present if message subject has changed
    ): EventResponseDto()



    @Serializable
    @SerialName(REALM_EVENT_TYPE)
    data class RealmEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("op")
        val op: OperationType,
    ): EventResponseDto() {
        @Serializable
        enum class OperationType(val value: String) {
            @SerialName("update")
            UPDATE("update"),
            @SerialName("deactivated")
            DEACTIVATED("deactivated"),
            @SerialName("update_dict")
            UPDATE_DICT("update_dict"),
        }
    }

    @Serializable
    @SerialName(HEARTBEAT_EVENT_TYPE)
    data class HeartbeatEventDto(
        @SerialName("id")
        override val id: Long,
    ): EventResponseDto()

    @Serializable
    @SerialName(SUBSCRIPTION_EVENT_TYPE)
    data class UserSubscriptionEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("op")
        val op: OperationType,
        @SerialName("subscriptions")
        // only affected subscriptions. Will be null if op is not "add" or "remove"
        val subscriptions: List<StreamResponseDto>? = null,
    ): EventResponseDto() {
        @Serializable
        enum class OperationType(val value: String) {
            @SerialName("add")
            ADD("add"),
            @SerialName("remove")
            REMOVE("remove"),
            @SerialName("update")
            UPDATE("update"),
            @SerialName("peer_add")
            PEER_ADD("peer_add"),
            @SerialName("peer_remove")
            PEER_REMOVE("peer_remove"),
        }
    }

    @Serializable
    @SerialName(PRESENCE_EVENT_TYPE)
    data class PresenceEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("presence")
        val presence: WebsitePresenceSourceDto,
        @SerialName("user_id")
        val userId: Long,
        @SerialName("email")
        val email: String,
        @SerialName("server_timestamp")
        val serverTimestamp: Double,

    ): EventResponseDto()

    @Serializable
    @SerialName(USER_STATUS_EVENT_TYPE)
    data class UserStatusEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("emoji_code")
        val emojiCode: String,
        @SerialName("emoji_name")
        val emojiName: String,
        @SerialName("reaction_type")
        val reactionType: String,
        @SerialName("status_text")
        val statusText: String,
        @SerialName("user_id")
        val userId: Long,
    ): EventResponseDto()

    @Serializable
    @SerialName(STREAM_EVENT_TYPE)
    data class StreamEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("op")
        val op: OperationType, // create, update, delete,
        @SerialName("streams")
        val streams: List<StreamResponseDto>? = null, // will present only in create/delete ops
        @SerialName("name")
        val streamName: String? = null, // will present only in update op
        @SerialName("stream_id")
        val streamId: Long? = null, // will present only in update op
    ): EventResponseDto() {
        @Serializable
        enum class OperationType(val value: String) {
            @SerialName("create")
            CREATE("create"),
            @SerialName("update")
            UPDATE("update"),
            @SerialName("delete")
            DELETE("delete"),
        }
    }

    @Serializable
    @SerialName(REACTION_EVENT_TYPE)
    data class ReactionEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("op")
        val op: OperationType, // add, remove
        @SerialName("emoji_code")
        val emojiCode: String,
        @SerialName("emoji_name")
        val emojiName: String,
        @SerialName("message_id")
        val messageId: Long,
        @SerialName("reaction_type")
        val reactionType: String,
        @SerialName("user_id")
        val userId: Long,
    ): EventResponseDto() {
        @Serializable
        enum class OperationType(val value: String) {
            @SerialName("add")
            ADD("add"),
            @SerialName("remove")
            REMOVE("remove"),
        }
    }

    @Serializable
    @SerialName(TYPING_EVENT_TYPE)
    data class TypingEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("op")
        val op: OperationType, // start, stop
        @SerialName("message_type")
        val messageType: String, // stream, direct
        @SerialName("stream_id")
        val streamId: Long? = null, // only present if message_type is stream
        @SerialName("topic")
        val topic: String? = null, // only present if message_type is stream
       @SerialName("sender")
        val sender: SenderResponseDto
    ): EventResponseDto() {
        @Serializable
        enum class OperationType(val value: String) {
            @SerialName("start")
            START("start"),
            @SerialName("stop")
            STOP("stop"),
        }
    }

    @Serializable
    @SerialName(UPDATE_MESSAGE_FLAGS_EVENT_TYPE)
    data class UpdateMessageFlagsEventDto(
        @SerialName("id")
        override val id: Long,
        @SerialName("flag")
        val flag: String,
        @SerialName("op")
        val op: OperationType, // add, remove
        @SerialName("messages")
        val messagesIds: List<Long>,
        @Serializable(with = MessageDetailsResponseDtoMapSerializer::class)
        @SerialName("message_details")
        val messageIdToMessageDetails: Map<String, MessageDetailsResponseDto>? = null, // Will only present on remove action
    ): EventResponseDto() {
        @Serializable
        enum class OperationType(val value: String) {
            @SerialName("add")
            ADD("add"),
            @SerialName("remove")
            REMOVE("remove"),
        }
    }

    companion object {
        const val REALM_EVENT_TYPE = "realm"
        const val HEARTBEAT_EVENT_TYPE = "heartbeat"

        const val PRESENCE_EVENT_TYPE = "presence"
        const val USER_STATUS_EVENT_TYPE = "user_status"

        const val SUBSCRIPTION_EVENT_TYPE = "subscription"
        const val STREAM_EVENT_TYPE = "stream"

        const val MESSAGE_EVENT_TYPE = "message"
        const val DELETE_MESSAGE_EVENT_TYPE = "delete_message"
        const val UPDATE_MESSAGE_EVENT_TYPE = "update_message"
        const val REACTION_EVENT_TYPE = "reaction"
        const val ATTACHMENT_EVENT_TYPE = "attachment"
        const val TYPING_EVENT_TYPE = "typing"

        const val UPDATE_MESSAGE_FLAGS_EVENT_TYPE = "update_message_flags"
    }
}
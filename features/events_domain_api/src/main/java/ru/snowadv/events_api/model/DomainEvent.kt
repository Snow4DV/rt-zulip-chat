package ru.snowadv.events_api.model

sealed class DomainEvent {
    abstract val id: Long
    abstract val queueId: String?
    open val senderType get() = EventSenderType.SERVER_SIDE

    // Synthetic client-side events

    data class FailedFetchingQueueEvent(
        override val id: Long,
        override val queueId: String?,
        val reason: Throwable,
        val isQueueBad: Boolean, // this determines if queue id should be erased from state
    ): DomainEvent() {
        override val senderType: EventSenderType
            get() = EventSenderType.SYNTHETIC_FAIL
    }

    data class RegisteredNewQueueEvent(
        override val queueId: String,
        override val id: Long,
        val timeoutSeconds: Int,
        // Will only be present if update_message_flags and message event types are obtained
        val streamUnreadMessages: List<EventStreamUpdateFlagsMessages>? = null,
    ): DomainEvent() {
        override val senderType: EventSenderType
            get() = EventSenderType.SYNTHETIC_REGISTER
    }

    // Real events

    data class MessageDomainEvent(
        override val id: Long,
        val eventMessage: EventMessage,
        override val queueId: String,
        val flags: Set<String>,
    ): DomainEvent()

    data class DeleteMessageDomainEvent(
        override val id: Long,
        val messageId: Long,
        override val queueId: String,
        val streamId: Long? = null, // can be absent if type is "private"
        val topic: String? = null, // can be absent if type is "private"
        val messageType: MessageType, // "stream" or "private"
    ): DomainEvent() {
        enum class MessageType(val value: String) {
            STREAM("stream"),
            PRIVATE("private"),
        }
    }

    data class UpdateMessageDomainEvent(
        override val id: Long,
        val messageId: Long,
        val content: String?, // Will only present if message content has changed.
        val subject: String?, // Will only present if subject has changed
        override val queueId: String,
    ): DomainEvent()

    data class RealmDomainEvent(
        override val id: Long,
        val op: OperationType,
        override val queueId: String,
    ): DomainEvent() {
        enum class OperationType(val value: String) {
            UPDATE("update"),
            DEACTIVATED("deactivated"),
            UPDATE_DICT("update_dict"),
        }
    }

    data class HeartbeatDomainEvent(
        override val id: Long,
        override val queueId: String,
    ): DomainEvent()

    data class UserSubscriptionDomainEvent(
        override val id: Long,
        override val queueId: String,
        val op: OperationType,
        val subscriptions: List<EventStream>? = null, // only affected subscriptions. Will be null if op is not "add" or "update"
    ): DomainEvent() {
        enum class OperationType(val value: String) {
            ADD("add"),
            REMOVE("remove"),
            UPDATE("update"),
            PEER_ADD("peer_add"),
            PEER_REMOVE("peer_remove"),
        }
    }

    data class PresenceDomainEvent(
        override val id: Long,
        override val queueId: String,
        val presence: EventPresence,
        val userId: Long,
        val email: String,
        val currentUser: Boolean,
    ): DomainEvent()

    data class UserStatusDomainEvent(
        override val id: Long,
        override val queueId: String,
        val emojiCode: String,
        val emojiName: String,
        val reactionType: String,
        val statusText: String,
        val userId: Long,
    ): DomainEvent()

    data class StreamDomainEvent(
        override val id: Long,
        override val queueId: String,
        val op: OperationType,
        val streams: List<EventStream>? = null, // will present only in create/delete ops
        val streamName: String? = null, // will present only in update op
        val streamId: Long? = null, // will present only in update op
    ): DomainEvent() {
        enum class OperationType(val value: String) {
            CREATE("create"),
            UPDATE("update"),
            DELETE("delete"),
        }
    }

    data class ReactionDomainEvent(
        override val id: Long,
        override val queueId: String,
        val op: OperationType, // add, remove
        val emojiCode: String,
        val emojiName: String,
        val messageId: Long,
        val reactionType: String,
        val userId: Long,
        val currentUserReaction: Boolean,
    ): DomainEvent() {
        enum class OperationType(val value: String) {
            ADD("add"),
            REMOVE("remove"),
        }
    }

    data class TypingDomainEvent(
        override val id: Long,
        override val queueId: String,
        val op: OperationType, // start, stop
        val messageType: String, // stream, direct
        val streamId: Long? = null, // only present if message_type is stream
        val topic: String? = null, // only present if message_type is stream
        val userId: Long,
        val userEmail: String,
    ): DomainEvent() {
        enum class OperationType(val value: String) {
            START("start"),
            STOP("stop"),
        }
    }


    data class AddMessageFlagEvent(
        override val id: Long,
        override val queueId: String,
        val flag: String,
        val addFlagMessagesIds: List<Long>,
    ): DomainEvent()

    data class RemoveMessageFlagEvent(
        override val id: Long,
        override val queueId: String,
        val flag: String,
        val removeFlagMessagesIds: List<Long>,
    ): DomainEvent()

    data class AddReadMessageFlagEvent(
        override val id: Long,
        val addFlagMessagesIds: List<Long>,
        override val queueId: String,
    ): DomainEvent()

    data class RemoveReadMessageFlagEvent(
        override val id: Long,
        override val queueId: String,
        val removeFlagMessagesIds: List<Long>,
        val removeFlagMessages: List<EventStreamUpdateFlagsMessages>,
    ): DomainEvent()
}

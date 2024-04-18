package ru.snowadv.event_api.model

sealed class DomainEvent {
    abstract val id: Long

    data class MessageDomainEvent(
        override val id: Long,
        val eventMessage: EventMessage
    ): DomainEvent()

    data class DeleteMessageDomainEvent(
        override val id: Long,
        val messageId: Long,
    ): DomainEvent()

    data class UpdateMessageDomainEvent(
        override val id: Long,
        val messageId: Long,
        val content: String? // Will only present if message content has changed.
    ): DomainEvent()

    data class RealmDomainEvent(
        override val id: Long,
        val op: String,
    ): DomainEvent()

    data class HeartbeatDomainEvent(
        override val id: Long,
    ): DomainEvent()

    data class UserSubscriptionDomainEvent(
        override val id: Long,
        val op: String, // will be "remove", "add", "update", "peer_add" or "peer_remove"
        val subscriptions: List<EventStream>? = null,
    ): DomainEvent()

    data class PresenceDomainEvent(
        override val id: Long,
        val presence: EventPresence,
        val userId: Long,
        val email: String,
        val currentUser: Boolean,
    ): DomainEvent()

    data class UserStatusDomainEvent(
        override val id: Long,
        val emojiCode: String,
        val emojiName: String,
        val reactionType: String,
        val statusText: String,
        val userId: Long,
    ): DomainEvent()

    data class StreamDomainEvent(
        override val id: Long,
        val op: String, // create, update, delete,
        val streams: List<EventStream>? = null // will present only in create/delete ops
    ): DomainEvent()

    data class ReactionDomainEvent(
        override val id: Long,
        val op: String, // add, remove
        val emojiCode: String,
        val emojiName: String,
        val messageId: Long,
        val reactionType: String,
        val userId: Long,
        val currentUserReaction: Boolean,
    ): DomainEvent()

    data class TypingDomainEvent(
        override val id: Long,
        val op: String, // start, stop
        val messageType: String, // stream, direct
        val streamId: Long? = null, // only present if message_type is stream
        val topic: String? = null, // only present if message_type is stream
        val userId: Long,
        val userEmail: String,
    ): DomainEvent()

    data class UnreadMessagesEvent(
        override val id: Long,
        val streamUnreadMessages: List<EventStreamUpdateFlagsMessages>,
    ): DomainEvent()



    data class AddMessageFlagEvent(
        override val id: Long,
        val flag: String,
        val addFlagMessagesIds: List<Long>,
    ): DomainEvent()

    data class RemoveMessageFlagEvent(
        override val id: Long,
        val flag: String,
        val removeFlagMessagesIds: List<Long>,
    ): DomainEvent()

    data class AddReadMessageFlagEvent(
        override val id: Long,
        val addFlagMessagesIds: List<Long>,
    ): DomainEvent()

    data class RemoveReadMessageFlagEvent(
        override val id: Long,
        val removeFlagMessagesIds: List<Long>,
        val removeFlagMessages: List<EventStreamUpdateFlagsMessages>,
    ): DomainEvent()
}

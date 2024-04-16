package ru.snowadv.event_api.model

import kotlin.reflect.KClass

enum class EventType(val apiName: String, type: KClass<*>) {
    REALM("realm", DomainEvent.RealmDomainEvent::class),
    HEARTBEAT("heartbeat", DomainEvent.HeartbeatDomainEvent::class),
    PRESENCE("presence", DomainEvent.PresenceDomainEvent::class),
    USER_STATUS("user_status", DomainEvent.UserStatusDomainEvent::class),
    SUBSCRIPTION("subscription", DomainEvent.UserSubscriptionDomainEvent::class),
    STREAM("stream", DomainEvent.StreamDomainEvent::class),
    MESSAGE("message", DomainEvent.MessageDomainEvent::class),
    DELETE_MESSAGE("delete_message", DomainEvent.DeleteMessageDomainEvent::class),
    UPDATE_MESSAGE("update_message", DomainEvent.UpdateMessageDomainEvent::class),
    REACTION("reaction", DomainEvent.ReactionDomainEvent::class),
    TYPING("typing", DomainEvent.TypingDomainEvent::class),
    UPDATE_MESSAGE_FLAGS("update_message_flags", DomainEvent.UpdateMessageFlagsEvent::class),
}
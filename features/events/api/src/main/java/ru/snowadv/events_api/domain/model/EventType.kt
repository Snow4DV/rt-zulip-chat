package ru.snowadv.events_api.domain.model

enum class EventType(val apiName: String) {
    REALM("realm"),
    HEARTBEAT("heartbeat"),
    PRESENCE("presence"),
    USER_STATUS("user_status"),
    SUBSCRIPTION("subscription"),
    STREAM("stream"),
    MESSAGE("message"),
    DELETE_MESSAGE("delete_message"),
    UPDATE_MESSAGE("update_message"),
    REACTION("reaction"),
    TYPING("typing"),
    UPDATE_MESSAGE_FLAGS("update_message_flags"),
}
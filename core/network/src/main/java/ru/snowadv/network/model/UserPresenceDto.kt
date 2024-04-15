package ru.snowadv.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllUsersPresenceDto(
    @SerialName("presences")
    val userEmailToPresenceSources: Map<String,PresenceSourcesDto>, // user email to presence sources
    @SerialName("server_timestamp")
    val serverTimestamp: Double,
)

@Serializable
data class SingleUserPresenceDto(
    @SerialName("presence")
    val presence: PresenceSourcesDto,
)

/**
 * Use aggregated field to get current presence.
 *
 * Starting with Zulip 7.0 (feature level 178), this will always contain two keys, "website" and
 * "aggregated", with identical data. The server no longer stores which client submitted presence
 * updates.
 */
@Serializable
data class PresenceSourcesDto(
    @SerialName("aggregated")
    val aggregated: UserPresenceDto,
    @SerialName("website")
    val website: UserPresenceDto,
)

@Serializable
data class WebsitePresenceSourceDto(
    @SerialName("website")
    val website: UserPresenceDto,
)

@Serializable
data class UserPresenceDto(
    /*
    Whether the user had recently interacted with Zulip at the time of the timestamp.
    Will be either "active" or "idle"
     */
    @SerialName("status")
    val status: String,
    /*
    When this update was received. UTC timezone/epoch seconds!!
    If the timestamp is more than a few minutes in the past, the user is offline.
     */
    @SerialName("timestamp")
    val timestamp: Double,
)
package ru.snowadv.users_data_api.model

import java.time.Instant

enum class DataUserStatus(val apiName: String) {
    ONLINE("active"),
    IDLE("idle"),
    OFFLINE("offline"),
    UNKNOWN("");

    companion object {
        // This variable describes how long can user be inactive to still be considered online/idle
        private const val ONLINE_DELTA_SEC = 60 * 5
        fun fromTimestampAndStatus(
            timestamp: Long,
            apiStatus: String,
            serverTimestamp: Long = Instant.now().epochSecond, // Server timestamp is not always available
        ): DataUserStatus {
            return when {
                (serverTimestamp - timestamp) > ONLINE_DELTA_SEC -> OFFLINE
                apiStatus == ONLINE.apiName -> ONLINE
                apiStatus == IDLE.apiName -> IDLE
                else -> OFFLINE
            }
        }

        fun fromStatus(
            apiStatus: String,
        ): DataUserStatus {
            return entries.first { it.apiName == apiStatus }
        }
    }
}
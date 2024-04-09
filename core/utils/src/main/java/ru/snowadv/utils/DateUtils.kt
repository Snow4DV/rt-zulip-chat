package ru.snowadv.utils

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime

object DateUtils {
    private val utcZoneId = ZoneId.of("UTC")

    fun epochSecondsToZonedDateTime(epochSeconds: Long): ZonedDateTime {
        val instant = Instant.ofEpochSecond(epochSeconds)
        return ZonedDateTime.ofInstant(instant, utcZoneId)
    }
}
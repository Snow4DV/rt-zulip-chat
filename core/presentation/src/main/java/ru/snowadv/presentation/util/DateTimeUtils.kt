package ru.snowadv.presentation.util

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

fun ZonedDateTime.toLocalDateTimeWithCurrentZone(): LocalDateTime {
    return this.withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime()
}
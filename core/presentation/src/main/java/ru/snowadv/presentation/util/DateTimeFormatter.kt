package ru.snowadv.presentation.util

import java.time.LocalDateTime

interface DateTimeFormatter {
    fun format(localDateTime: LocalDateTime): String
}
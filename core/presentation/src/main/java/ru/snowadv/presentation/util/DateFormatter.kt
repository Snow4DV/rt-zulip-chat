package ru.snowadv.presentation.util

import java.time.LocalDate

interface DateFormatter {
    fun format(date: LocalDate): String
}
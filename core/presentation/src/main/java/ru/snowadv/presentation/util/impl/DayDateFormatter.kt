package ru.snowadv.presentation.util.impl

import android.content.Context
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale
import java.time.format.DateTimeFormatter as JavaDateTimeFormatter

/**
 * This formatter formats date like "23 Feb" or "23 Фев"
 */
class DayDateFormatter(context: Context): DateFormatter {

    companion object {
        const val FORMAT = "MMM dd"
    }

    private val primaryLocale = Locale.US // The only locale supported in the app at the moment

    private val formatter =
        JavaDateTimeFormatter.ofPattern(FORMAT, primaryLocale)

    override fun format(date: LocalDate): String {
        return formatter.format(date)
    }
}
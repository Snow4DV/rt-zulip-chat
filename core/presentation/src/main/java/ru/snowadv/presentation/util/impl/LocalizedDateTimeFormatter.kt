package ru.snowadv.presentation.util.impl

import android.content.Context
import ru.snowadv.presentation.R
import ru.snowadv.presentation.util.DateTimeFormatter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter as JavaDateTimeFormatter

/**
 * This formatter formats LocalDateTime objects to strings according to R.string.timestamp_format resource
 */
class LocalizedDateTimeFormatter(context: Context): DateTimeFormatter {

    private val formatter =
        JavaDateTimeFormatter.ofPattern(context.getString(R.string.timestamp_format));

    override fun format(localDateTime: LocalDateTime): String {
        return formatter.format(localDateTime)
    }
}
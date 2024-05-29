package ru.snowadv.chatapp.glue.network

import ru.snowadv.model.LoggerToggle
import javax.inject.Inject

internal class ReleaseLoggerToggle @Inject constructor(): LoggerToggle {
    override val isLoggingEnabled: Boolean = false
}
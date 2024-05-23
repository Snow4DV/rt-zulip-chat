package ru.snowadv.chatapp.glue.network

import ru.snowadv.network.api.LoggerToggle
import javax.inject.Inject

internal class ReleaseLoggerToggle @Inject constructor(): LoggerToggle {
    override val isLoggingEnabled: Boolean = true
}
package ru.snowadv.chatapp.config

import ru.snowadv.network.api.LoggerToggle

internal object DebugLoggerToggle: LoggerToggle {
    override val isLoggingEnabled: Boolean = true
}
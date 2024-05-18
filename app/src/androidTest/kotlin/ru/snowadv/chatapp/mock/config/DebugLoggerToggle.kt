package ru.snowadv.chatapp.mock.config

import dagger.Reusable
import ru.snowadv.network.api.LoggerToggle
import javax.inject.Inject

@Reusable
internal class DebugLoggerToggle @Inject constructor(): LoggerToggle {
    override val isLoggingEnabled: Boolean = true
}
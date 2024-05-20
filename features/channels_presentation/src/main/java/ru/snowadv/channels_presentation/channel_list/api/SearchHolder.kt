package ru.snowadv.channels_presentation.channel_list.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

internal interface SearchHolder {
    val searchQuery: Flow<String>
}
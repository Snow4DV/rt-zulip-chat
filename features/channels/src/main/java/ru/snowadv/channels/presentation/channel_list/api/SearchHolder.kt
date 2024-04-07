package ru.snowadv.channels.presentation.channel_list.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface SearchHolder {
    val searchQuery: StateFlow<String>
}
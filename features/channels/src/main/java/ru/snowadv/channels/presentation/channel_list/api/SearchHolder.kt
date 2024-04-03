package ru.snowadv.channels.presentation.channel_list.api

import kotlinx.coroutines.flow.Flow

interface SearchHolder {
    val searchQuery: Flow<String>
}
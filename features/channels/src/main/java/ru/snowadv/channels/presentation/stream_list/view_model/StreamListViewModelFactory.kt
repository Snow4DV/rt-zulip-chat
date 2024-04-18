package ru.snowadv.channels.presentation.stream_list.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.snowadv.channels.di.ChannelsGraph
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.domain.navigation.ChannelsRouter
import ru.snowadv.channels.domain.use_case.GetStreamsUseCase
import ru.snowadv.channels.domain.use_case.GetTopicsUseCase
import ru.snowadv.channels.domain.use_case.ListenToStreamEventsUseCase
import ru.snowadv.channels.presentation.channel_list.view_model.ChannelListViewModel

internal class StreamListViewModelFactory(
    private val streamType: StreamType,
    private val router: ChannelsRouter,
    private val searchQueryFlow: StateFlow<String>,
    private val getStreamsUseCase: GetStreamsUseCase,
    private val getTopicsUseCase: GetTopicsUseCase,
    private val listenToStreamEventsUseCase: ListenToStreamEventsUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StreamListViewModel::class.java)) {
            return StreamListViewModel(
                type = streamType,
                router = router,
                searchQueryFlow = searchQueryFlow,
                getStreamsUseCase = getStreamsUseCase,
                getTopicsUseCase =  getTopicsUseCase,
                listenToStreamEventsUseCase = listenToStreamEventsUseCase,
                defaultDispatcher = defaultDispatcher,
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
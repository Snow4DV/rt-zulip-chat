package ru.snowadv.channels.presentation.stream_list.event

sealed class StreamListFragmentEvent {
    class ShowInternetErrorWithRetry(val retryAction: () -> Unit): StreamListFragmentEvent()
    data object ShowInternetError: StreamListFragmentEvent()
}
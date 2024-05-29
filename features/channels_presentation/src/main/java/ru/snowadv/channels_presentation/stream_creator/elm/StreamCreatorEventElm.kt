package ru.snowadv.channels_presentation.stream_creator.elm

internal sealed interface StreamCreatorEventElm {
    sealed interface Ui : StreamCreatorEventElm {
        data class OnStreamNameChanged(val newName: String) : Ui
        data class OnStreamDescriptionChanged(val newDescription: String): Ui
        data class OnStreamAnnounceChanged(val newAnnounce: Boolean): Ui
        data class OnStreamHistoryAvailableToNewSubsChanged(val newState: Boolean): Ui
        data object OnCreateStreamClicked: Ui
    }

    sealed interface Internal : StreamCreatorEventElm {
        data object CreatingStream : Internal
        data class StreamCreationError(val error: Throwable) : Internal
        data object StreamCreated : Internal
    }
}
package ru.snowadv.channels_presentation.stream_creator.elm

import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class StreamCreatorReducerElm @Inject constructor():
    ScreenDslReducer<StreamCreatorEventElm, StreamCreatorEventElm.Ui, StreamCreatorEventElm.Internal, StreamCreatorStateElm, StreamCreatorEffectElm, StreamCreatorCommandElm>(
        uiEventClass = StreamCreatorEventElm.Ui::class,
        internalEventClass = StreamCreatorEventElm.Internal::class,
    ) {
    override fun Result.internal(event: StreamCreatorEventElm.Internal) {
        when(event) {
            StreamCreatorEventElm.Internal.CreatingStream -> state {
                copy(creatingStream = true)
            }
            StreamCreatorEventElm.Internal.StreamCreated -> {
                state {
                    copy(creatingStream = false)
                }
                effects {
                    +StreamCreatorEffectElm.CloseWithNewStreamCreated(state.streamName)
                }
            }
            is StreamCreatorEventElm.Internal.StreamCreationError -> {
                state {
                    copy(creatingStream = false)
                }
                effects {
                    +StreamCreatorEffectElm.ShowInternetError
                }
            }
        }
    }

    override fun Result.ui(event: StreamCreatorEventElm.Ui) {
        when(event) {
            StreamCreatorEventElm.Ui.OnCreateStreamClicked -> commands {
                +StreamCreatorCommandElm.CreateStream(
                    name = state.streamName,
                    description = state.description,
                    announce = state.announce,
                    isHistoryAvailableToSubscribers = state.isHistoryAvailableToNewSubs,
                )
            }

            is StreamCreatorEventElm.Ui.OnStreamAnnounceChanged -> state {
                copy(announce = event.newAnnounce)
            }
            is StreamCreatorEventElm.Ui.OnStreamDescriptionChanged -> state {
                copy(description = event.newDescription)
            }
            is StreamCreatorEventElm.Ui.OnStreamHistoryAvailableToNewSubsChanged -> state {
                copy(isHistoryAvailableToNewSubs = event.newState)
            }
            is StreamCreatorEventElm.Ui.OnStreamNameChanged -> state {
                copy(streamName = event.newName)
            }
        }
    }
}
package ru.snowadv.channels_presentation.stream_creator.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.snowadv.channels_domain_api.use_case.CreateStreamUseCase

import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class StreamCreatorActorElm @Inject constructor(
    private val createStreamUseCase: CreateStreamUseCase,
) : Actor<StreamCreatorCommandElm, StreamCreatorEventElm>() {
    override fun execute(command: StreamCreatorCommandElm): Flow<StreamCreatorEventElm> {
        return when (command) {
            is StreamCreatorCommandElm.CreateStream -> {
                createStreamUseCase(
                    name = command.name,
                    description = command.description,
                    announce = command.announce,
                    isHistoryAvailableToNewSubs = command.isHistoryAvailableToSubscribers,
                ).mapEvents(
                    eventMapper = { res ->
                        when (res) {
                            is Resource.Error -> StreamCreatorEventElm.Internal.StreamCreationError(
                                res.throwable
                            )
                            is Resource.Loading -> StreamCreatorEventElm.Internal.CreatingStream
                            is Resource.Success -> StreamCreatorEventElm.Internal.StreamCreated
                        }
                    },
                    errorMapper = {
                        StreamCreatorEventElm.Internal.StreamCreationError(it)
                    }
                )
            }
        }
    }
}
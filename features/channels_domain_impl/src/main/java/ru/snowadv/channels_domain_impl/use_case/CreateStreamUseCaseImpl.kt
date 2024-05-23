package ru.snowadv.channels_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.channels_domain_api.use_case.CreateStreamUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class CreateStreamUseCaseImpl @Inject constructor(
    private val streamsRepo: StreamRepository,
): CreateStreamUseCase {
    override fun invoke(
        name: String,
        description: String,
        announce: Boolean,
        isHistoryAvailableToNewSubs: Boolean
    ): Flow<Resource<Unit>> {
        return streamsRepo.createStream(
            name = name,
            description = description,
            announce = announce,
            showHistoryToNewSubs = isHistoryAvailableToNewSubs,
        )
    }

}
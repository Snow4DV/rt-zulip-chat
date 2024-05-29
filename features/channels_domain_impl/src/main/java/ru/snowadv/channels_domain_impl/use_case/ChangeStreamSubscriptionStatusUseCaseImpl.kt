package ru.snowadv.channels_domain_impl.use_case

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.channels_domain_api.repository.StreamRepository
import ru.snowadv.channels_domain_api.use_case.ChangeStreamSubscriptionStatusUseCase
import ru.snowadv.model.Resource
import javax.inject.Inject

@Reusable
internal class ChangeStreamSubscriptionStatusUseCaseImpl @Inject constructor(
    private val streamsRepo: StreamRepository,
): ChangeStreamSubscriptionStatusUseCase {
    override fun invoke(
        name: String,
        newSubscriptionStatus: Boolean,
    ): Flow<Resource<Unit>> {
        return if (newSubscriptionStatus) {
            streamsRepo.subscribeToStream(name = name)
        } else {
            streamsRepo.unsubscribeFromStream(name = name)
        }
    }
}
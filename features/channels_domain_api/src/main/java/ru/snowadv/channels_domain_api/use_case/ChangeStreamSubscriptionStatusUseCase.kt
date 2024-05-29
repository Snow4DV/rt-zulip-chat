package ru.snowadv.channels_domain_api.use_case

import kotlinx.coroutines.flow.Flow
import ru.snowadv.model.Resource

interface ChangeStreamSubscriptionStatusUseCase {
    operator fun invoke(name: String, newSubscriptionStatus: Boolean): Flow<Resource<Unit>>
}
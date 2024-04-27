package ru.snowadv.profile.presentation.profile.elm

import dagger.Reusable
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class ProfileStoreFactoryElm @Inject constructor(
    private val actor: ProfileActorElm,
    private val reducerElm: Provider<ProfileReducerElm>,
) {

    fun create(profileId: Long?): Store<ProfileEventElm, ProfileEffectElm, ProfileStateElm> {
        return ElmStore(
            initialState = ProfileStateElm(profileId = profileId, eventQueueData = null, isResumed = false),
            actor = actor,
            reducer = reducerElm.get(),
        )
    }
}
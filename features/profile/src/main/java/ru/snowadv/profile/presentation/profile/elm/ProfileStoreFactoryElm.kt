package ru.snowadv.profile.presentation.profile.elm

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class ProfileStoreFactoryElm(
    private val actor: ProfileActorElm,
    private val profileId: Long?,
) {

    fun create(): Store<ProfileEventElm, ProfileEffectElm, ProfileStateElm> {
        return ElmStore(
            initialState = ProfileStateElm(profileId = profileId, eventQueueData = null, isResumed = false),
            actor = actor,
            reducer = ProfileReducerElm(),
        )
    }
}
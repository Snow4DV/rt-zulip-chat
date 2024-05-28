package ru.snowadv.home_presentation.home.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class HomeActorElm @Inject constructor(): Actor<HomeCommandElm, HomeEventElm>() {
    override fun execute(command: HomeCommandElm): Flow<HomeEventElm> = flow {}
}
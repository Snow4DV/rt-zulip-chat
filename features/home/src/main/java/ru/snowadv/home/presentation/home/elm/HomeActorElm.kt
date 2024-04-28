package ru.snowadv.home.presentation.home.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor

internal class HomeActorElm: Actor<HomeCommandElm, HomeEventElm>() {
    override fun execute(command: HomeCommandElm): Flow<HomeEventElm> = flow {}
}
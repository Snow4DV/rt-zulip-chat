package ru.snowadv.people_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.people_api.presentation.PeopleScreenFactory
import ru.snowadv.people_impl.presentation.feature.PeopleScreenFactoryImpl
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListActorElm
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListCommandElm
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListEffectElm
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListEventElm
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListReducerElm
import ru.snowadv.people_impl.presentation.people_list.elm.PeopleListStateElm
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface PeopleFeatureModule {
    @Binds
    fun bindPeopleScreenFactoryElm(peopleScreenFactoryImpl: PeopleScreenFactoryImpl): PeopleScreenFactory
    @Binds
    fun bindPeopleListActorElm(peopleListActorElm: PeopleListActorElm): Actor<PeopleListCommandElm, PeopleListEventElm>
    @Binds
    fun bindPeopleListReducerElm(peopleListReducerElm: PeopleListReducerElm): ScreenDslReducer<PeopleListEventElm, PeopleListEventElm.Ui, PeopleListEventElm.Internal, PeopleListStateElm, PeopleListEffectElm, PeopleListCommandElm>
}
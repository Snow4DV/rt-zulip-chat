package ru.snowadv.home_presentation.di.dagger

import dagger.Binds
import dagger.Module
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface HomePresentationModule {
    @Binds
    fun bindHomeScreenFactoryImpl(homeScreenFactoryImpl: ru.snowadv.home_presentation.feature.HomeScreenFactoryImpl): ru.snowadv.home_presentation.api.HomeScreenFactory
    @Binds
    fun bindHomeActorElm(homeActorElm: ru.snowadv.home_presentation.home.elm.HomeActorElm): Actor<ru.snowadv.home_presentation.home.elm.HomeCommandElm, ru.snowadv.home_presentation.home.elm.HomeEventElm>
    @Binds
    fun bindHomeReducerElm(homeReducerElm: ru.snowadv.home_presentation.home.elm.HomeReducerElm): ScreenDslReducer<ru.snowadv.home_presentation.home.elm.HomeEventElm, ru.snowadv.home_presentation.home.elm.HomeEventElm.Ui, ru.snowadv.home_presentation.home.elm.HomeEventElm.Internal, ru.snowadv.home_presentation.home.elm.HomeStateElm, ru.snowadv.home_presentation.home.elm.HomeEffectElm, ru.snowadv.home_presentation.home.elm.HomeCommandElm>
    @Binds
    fun bindInnerHomeScreenFactoryImpl(innerHomeScreenFactoryImpl: ru.snowadv.home_presentation.local_navigation.impl.InnerHomeScreenFactoryImpl): ru.snowadv.home_presentation.local_navigation.InnerHomeScreenFactory
}
package ru.snowadv.users_domain_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.users_domain_api.use_case.GetPeopleUseCase
import ru.snowadv.users_domain_api.use_case.GetProfileUseCase
import ru.snowadv.users_domain_api.use_case.ListenToPeoplePresenceEventsUseCase
import ru.snowadv.users_domain_api.use_case.ListenToProfilePresenceEventsUseCase
import ru.snowadv.users_domain_impl.use_case.GetPeopleUseCaseImpl
import ru.snowadv.users_domain_impl.use_case.GetProfileUseCaseImpl
import ru.snowadv.users_domain_impl.use_case.ListenToPeoplePresenceEventsUseCaseImpl
import ru.snowadv.users_domain_impl.use_case.ListenToProfilePresenceEventsUseCaseImpl


@Module
internal interface UsersDomainModule {
    @Binds
    fun bindGetPeopleUseCaseImpl(impl: GetPeopleUseCaseImpl): GetPeopleUseCase
    @Binds
    fun bindGetProfileUseCaseImpl(impl: GetProfileUseCaseImpl): GetProfileUseCase
    @Binds
    fun bindListenToPeoplePresenceUseCaseImpl(impl: ListenToPeoplePresenceEventsUseCaseImpl): ListenToPeoplePresenceEventsUseCase
    @Binds
    fun bindListenToProfilePresenceUseCaseImpl(impl: ListenToProfilePresenceEventsUseCaseImpl): ListenToProfilePresenceEventsUseCase
}
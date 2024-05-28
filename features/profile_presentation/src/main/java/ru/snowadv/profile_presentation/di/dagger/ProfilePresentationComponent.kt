package ru.snowadv.profile_presentation.di.dagger

import dagger.Component
import ru.snowadv.module_injector.general.PerScreen
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationAPI
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationDependencies
import ru.snowadv.profile_presentation.ui.ProfileFragment
import ru.snowadv.profile_presentation.ui.ProfileFragmentRenderer

@Component(dependencies = [ProfilePresentationDependencies::class], modules = [ProfilePresentationModule::class])
@PerScreen
internal interface ProfilePresentationComponent : ProfilePresentationAPI {

    fun inject(profileFragment: ProfileFragment)
    fun inject(profileFragmentRenderer: ProfileFragmentRenderer)
    companion object {
        fun initAndGet(deps: ProfilePresentationDependencies): ProfilePresentationComponent {
            return DaggerProfilePresentationComponent.builder()
                .profilePresentationDependencies(deps)
                .build()
        }
    }
}
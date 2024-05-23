package ru.snowadv.profile_presentation.ui

import androidx.core.view.isVisible
import coil.load
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.profile_presentation.presentation.elm.ProfileStateElm
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.profile_presentation.R
import ru.snowadv.profile_presentation.presentation.elm.ProfileEffectElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileEventElm
import ru.snowadv.profile_presentation.databinding.FragmentProfileBinding
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationComponentHolder
import ru.snowadv.profile_presentation.ui.elm.ProfileEffectUiElm
import ru.snowadv.profile_presentation.ui.elm.ProfileEventUiElm
import ru.snowadv.profile_presentation.ui.elm.ProfileStateUiElm
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ProfileFragmentRenderer :
    ElmFragmentRenderer<ProfileFragment, FragmentProfileBinding, ProfileEventElm, ProfileEffectElm, ProfileStateElm> {

    @Inject
    internal lateinit var mapper: ElmMapper<ProfileStateElm, ProfileEffectElm, ProfileEventElm, ProfileStateUiElm, ProfileEffectUiElm, ProfileEventUiElm>

    override fun ProfileFragment.onAttachRendererView() {
        ProfilePresentationComponentHolder.getComponent().inject(this@ProfileFragmentRenderer)
    }

    override fun ProfileFragment.onRendererViewCreated(
        binding: FragmentProfileBinding,
        store: Store<ProfileEventElm, ProfileEffectElm, ProfileStateElm>,
    ) {
        initListeners(binding, store)
    }

    private fun initListeners(
        binding: FragmentProfileBinding,
        store: Store<ProfileEventElm, ProfileEffectElm, ProfileStateElm>
    ) {
        binding.profileTopBar.backButton.setOnClickListener {
            store.accept(mapper.mapUiEvent(ProfileEventUiElm.ClickedOnBack))
        }
        binding.stateBox.setOnRetryClickListener {
            store.accept(mapper.mapUiEvent(ProfileEventUiElm.ClickedOnBack))
        }
        binding.buttonLogout.setOnClickListener {
            store.accept(mapper.mapUiEvent(ProfileEventUiElm.ClickedOnLogout))
        }
    }

    override fun ProfileFragment.renderStateByRenderer(
        state: ProfileStateElm,
        binding: FragmentProfileBinding
    ): Unit = with(binding) {
        val mappedState = mapper.mapState(state)

        stateBox.inflateState(mappedState.screenState, R.layout.fragment_profile_shimmer, topStateBox)
        profileTopBar.root.isVisible = !mappedState.isOwner
        profileUserAvatar.isVisible = mappedState.screenState.getCurrentData() != null
        mappedState.screenState.getCurrentData()?.let {  person ->
            profileUserStatus.text = getString(person.status.displayNameResId)
            profileUserStatus.setTextColor(resources.getColor(person.status.colorResId, context?.theme))
            person.avatarUrl?.let { url ->
                profileUserAvatar.load(url) {
                    placeholder(ru.snowadv.presentation.R.drawable.ic_user_avatar)
                    crossfade(true)
                }
            }
            profileUserName.text = person.fullName
            profileUserEmail.text = person.email
        }
        buttonLogout.isVisible = mappedState.isOwner
    }
}
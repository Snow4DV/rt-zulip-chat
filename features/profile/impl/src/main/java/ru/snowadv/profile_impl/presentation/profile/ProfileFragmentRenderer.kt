package ru.snowadv.profile_impl.presentation.profile

import android.view.View
import coil.load
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileStateElm
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileEffectElm
import ru.snowadv.profile_impl.presentation.profile.elm.ProfileEventElm
import ru.snowadv.profile_impl.R
import ru.snowadv.profile_impl.databinding.FragmentProfileBinding
import vivid.money.elmslie.core.store.Store

internal class ProfileFragmentRenderer :
    ElmFragmentRenderer<ProfileFragment, FragmentProfileBinding, ProfileEventElm, ProfileEffectElm, ProfileStateElm> {

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
        binding.topBar.backButton.setOnClickListener {
            store.accept(ProfileEventElm.Ui.ClickedOnBack)
        }
        binding.stateBox.setOnRetryClickListener {
            store.accept(ProfileEventElm.Ui.ClickedOnRetry)
        }
    }

    override fun ProfileFragment.renderStateByRenderer(
        state: ProfileStateElm,
        binding: FragmentProfileBinding
    ): Unit = with(binding) {
        stateBox.inflateState(state.screenState, R.layout.fragment_profile_shimmer)
        topBar.root.visibility = if (state.isOwner) View.GONE else View.VISIBLE
        state.screenState.getCurrentData()?.let {  person ->
            userStatus.text = getString(person.status.displayNameResId)
            userStatus.setTextColor(resources.getColor(person.status.colorResId, context?.theme))
            person.avatarUrl?.let { url ->
                userAvatar.load(url) {
                    placeholder(ru.snowadv.presentation.R.drawable.ic_user_avatar)
                    crossfade(true)
                }
            }
            userName.text = person.fullName
            userEmail.text = person.email
        }
    }
}
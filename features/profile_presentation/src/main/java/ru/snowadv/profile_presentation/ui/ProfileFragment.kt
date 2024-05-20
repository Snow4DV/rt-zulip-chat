package ru.snowadv.profile_presentation.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.setColorAndText
import ru.snowadv.profile_presentation.R
import ru.snowadv.profile_presentation.presentation.elm.ProfileEffectElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileEventElm
import ru.snowadv.profile_presentation.presentation.elm.ProfileStateElm
import ru.snowadv.profile_presentation.databinding.FragmentProfileBinding
import ru.snowadv.profile_presentation.di.holder.ProfilePresentationComponentHolder
import ru.snowadv.profile_presentation.presentation.elm.ProfileStoreFactoryElm
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ProfileFragment : BaseFragment<ProfileEventElm, ProfileEffectElm, ProfileStateElm>(),
    ElmFragmentRenderer<ProfileFragment, FragmentProfileBinding, ProfileEventElm, ProfileEffectElm, ProfileStateElm> by ProfileFragmentRenderer() {
    companion object {
        const val ARG_PROFILE_ID_KEY = "profile_id"
        const val DEFAULT_PROFILE_ID = -1L

        /**
         * Pass either profileId to show other user's profile or null to display current user's profile
         */
        fun newInstance(profileId: Long? = null): Fragment = ProfileFragment().apply {
            profileId?.let { profileId ->
                arguments = bundleOf(
                    ARG_PROFILE_ID_KEY to profileId,
                )
            } ?: run {
                arguments = bundleOf()
            }
        }
    }

    @Inject
    internal lateinit var profileStoreFactoryElm: ProfileStoreFactoryElm

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding) {"Binding wasn't initialized"}

    private val profileId: Long? by lazy {
        requireArguments().getLong(ARG_PROFILE_ID_KEY, DEFAULT_PROFILE_ID).let {
            if (it == DEFAULT_PROFILE_ID) null else it
        }
    }
    override val store: Store<ProfileEventElm, ProfileEffectElm, ProfileStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        profileStoreFactoryElm.create(profileId)
    }
    override val resumeUiEvent: ProfileEventElm = ProfileEventElm.Ui.Resumed
    override val pauseUiEvent: ProfileEventElm = ProfileEventElm.Ui.Paused

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ProfilePresentationComponentHolder.getComponent().inject(this)
        onAttachRendererView()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentProfileBinding.inflate(inflater, container, false)
            .also { _binding = it }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
        binding.topBar.setColorAndText(ru.snowadv.presentation.R.color.on_surface, getString(R.string.profile))
        if (savedInstanceState == null) store.accept(ProfileEventElm.Ui.Init)
    }



    override fun render(state: ProfileStateElm) {
        renderStateByRenderer(state, binding)
    }

}
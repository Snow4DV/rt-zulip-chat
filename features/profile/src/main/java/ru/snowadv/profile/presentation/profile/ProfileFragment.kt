package ru.snowadv.profile.presentation.profile

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.snowadv.profile.R
import ru.snowadv.profile.databinding.FragmentProfileBinding
import ru.snowadv.profile.presentation.profile.view_model.ProfileViewModel
import ru.snowadv.profile.presentation.profile.view_model.ProfileViewModelFactory
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.setColorAndText
import ru.snowadv.profile.di.ProfileGraph

class ProfileFragment : Fragment(),
    FragmentDataObserver<FragmentProfileBinding, ProfileViewModel, ProfileFragment> by ProfileFragmentDataObserver() {

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

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = requireNotNull(_binding) {"Binding wasn't initialized"}

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(
            router = ProfileGraph.deps.router,
            profileId = profileId,
            profileRepository = ProfileGraph.deps.repo,
        )
    }

    private val profileId: Long? by lazy {
        requireArguments().getLong(ARG_PROFILE_ID_KEY, DEFAULT_PROFILE_ID).let {
            if (it == DEFAULT_PROFILE_ID) null else it
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentProfileBinding.inflate(inflater, container, false)
            .also { _binding = it }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel)
        binding.topBar.setColorAndText(ru.snowadv.presentation.R.color.on_surface, getString(R.string.profile))
    }

}
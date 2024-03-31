package ru.snowadv.home.presentation.profile

import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.snowadv.home.R
import ru.snowadv.home.databinding.FragmentPeopleBinding
import ru.snowadv.home.databinding.FragmentProfileBinding
import ru.snowadv.home.presentation.di.HomeGraph
import ru.snowadv.home.presentation.people_list.view_model.PeopleListViewModel
import ru.snowadv.home.presentation.people_list.view_model.PeopleListViewModelFactory
import ru.snowadv.home.presentation.profile.view_model.ProfileViewModel
import ru.snowadv.home.presentation.profile.view_model.ProfileViewModelFactory
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.setColorAndText
import ru.snowadv.presentation.fragment.setTopBarColor

class ProfileFragment : Fragment(),
    FragmentDataObserver<FragmentProfileBinding, ProfileViewModel, ProfileFragment> by ProfileFragmentDataObserver() {

    companion object {
        const val ARG_PROFILE_ID_KEY = "profile_id"
        const val ARG_IS_OWNER_KEY = "is_owner"
        const val DEFAULT_PROFILE_ID = -1L
        const val DEFAULT_IS_OWNER = false
        fun newInstance(profileId: Long, isOwner: Boolean): Fragment = ProfileFragment().apply {
            arguments = bundleOf(
                ARG_PROFILE_ID_KEY to profileId,
                ARG_IS_OWNER_KEY to isOwner
            )
        }
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels {
        ProfileViewModelFactory(
            HomeGraph.router,
            profileId,
            isOwner
        )
    }

    private val profileId by lazy {
        requireArguments().getLong(ARG_PROFILE_ID_KEY, DEFAULT_PROFILE_ID).also {
            if (it == DEFAULT_PROFILE_ID) error("No profileId argument passed to ProfileFragment")
        }
    }
    private val isOwner by lazy {
        requireArguments().getBoolean(ARG_IS_OWNER_KEY, DEFAULT_IS_OWNER)
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
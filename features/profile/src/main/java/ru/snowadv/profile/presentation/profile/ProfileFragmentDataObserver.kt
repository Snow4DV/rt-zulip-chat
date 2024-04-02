package ru.snowadv.profile.presentation.profile

import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import coil.load
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.profile.R
import ru.snowadv.profile.databinding.FragmentProfileBinding
import ru.snowadv.profile.presentation.profile.event.ProfileEvent
import ru.snowadv.profile.presentation.profile.state.ProfileScreenState
import ru.snowadv.profile.presentation.profile.view_model.ProfileViewModel
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.setNewState
import ru.snowadv.presentation.fragment.setOnRetryClickListener

internal class ProfileFragmentDataObserver :
    FragmentDataObserver<FragmentProfileBinding, ProfileViewModel, ProfileFragment> {

    override fun ProfileFragment.registerObservingFragment(
        binding: FragmentProfileBinding,
        viewModel: ProfileViewModel
    ) {
        observeState(binding, viewModel)
        initListeners(binding, viewModel)
    }

    private fun initListeners(
        binding: FragmentProfileBinding,
        viewModel: ProfileViewModel,
    ) {
        binding.buttonLogout.setOnClickListener {
            viewModel.handleEvent(ProfileEvent.ClickedOnLogout)
        }
        binding.topBar.backButton.setOnClickListener {
            viewModel.handleEvent(ProfileEvent.ClickedOnBack)
        }
        binding.stateBox.setOnRetryClickListener {
            viewModel.handleEvent(ProfileEvent.ClickedOnRetry)
        }
    }

    private fun ProfileFragment.observeState(
        binding: FragmentProfileBinding,
        viewModel: ProfileViewModel,
    ) {
        viewModel.state.onEach {
            bindState(binding, it)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(lifecycleScope)
    }

    private fun ProfileFragment.bindState(
        binding: FragmentProfileBinding,
        state: ProfileScreenState,
    ) = with(binding) {
        stateBox.setNewState(state.screenState)
        topBar.root.visibility = if (state.isOwner) View.GONE else View.VISIBLE
        buttonLogout.visibility = if (state.isOwner) View.VISIBLE else View.GONE
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
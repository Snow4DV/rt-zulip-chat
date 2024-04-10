package ru.snowadv.home.presentation.home

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.home.databinding.FragmentHomeBinding
import ru.snowadv.home.presentation.home.event.HomeScreenEvent
import ru.snowadv.home.presentation.home.state.HomeScreenState
import ru.snowadv.home.presentation.home.view_model.HomeViewModel
import ru.snowadv.home.presentation.local_navigation.HomeScreen
import ru.snowadv.presentation.fragment.FragmentDataObserver

class HomeFragmentDataObserver :
    FragmentDataObserver<FragmentHomeBinding, HomeViewModel, HomeFragment> {
    override fun HomeFragment.registerObservingFragment(
        binding: FragmentHomeBinding,
        viewModel: HomeViewModel
    ) {
        viewModel.state.onEach {
            bindState(it, binding)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(viewLifecycleOwner.lifecycleScope)
        initListeners(viewModel, binding)
    }

    private fun HomeFragment.bindState(
        state: HomeScreenState,
        binding: FragmentHomeBinding,
    ) {
        if (state.currentScreen.menuActionId != binding.bottomNavigation.selectedItemId) {
            binding.bottomNavigation.selectedItemId = state.currentScreen.menuActionId
        }
        selectTab(state.currentScreen)
    }

    private fun initListeners(
        viewModel: HomeViewModel,
        binding: FragmentHomeBinding,
    ) {

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            HomeScreen.getByActionId(menuItem.itemId).let { homeScreen ->
                viewModel.handleEvent(HomeScreenEvent.OnBottomTabSelected(homeScreen))
            }
            true
        }
    }
}
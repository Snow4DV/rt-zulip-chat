package ru.snowadv.home.presentation.channel_list

import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.home.databinding.FragmentChannelListBinding
import ru.snowadv.home.presentation.channel_list.event.ChannelListEvent
import ru.snowadv.home.presentation.channel_list.state.ChannelListScreenState
import ru.snowadv.home.presentation.channel_list.view_model.ChannelListSharedViewModel
import ru.snowadv.presentation.fragment.FragmentDataObserver

internal class ChannelListFragmentDataObserver :
    FragmentDataObserver<FragmentChannelListBinding, ChannelListSharedViewModel, ChannelListFragment> {

    override fun ChannelListFragment.registerObservingFragment(
        binding: FragmentChannelListBinding,
        viewModel: ChannelListSharedViewModel
    ) {
        observeState(binding, viewModel)
        initListeners(binding, viewModel)
    }

    private fun ChannelListFragment.observeState(
        binding: FragmentChannelListBinding,
        viewModel: ChannelListSharedViewModel,
    ) {
        viewModel.state.onEach {
            bindState(binding, it)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(lifecycleScope)
    }
    private fun bindState(
        binding: FragmentChannelListBinding,
        state: ChannelListScreenState,
    ) = with(binding) {
        if (searchBar.searchEditText.text.toString() != state.searchQuery) {
            searchBar.searchEditText.setText(state.searchQuery)
        }

    }

    private fun initListeners(
        binding: FragmentChannelListBinding,
        viewModel: ChannelListSharedViewModel,
    ) {
        binding.searchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let {  text ->
                if (viewModel.state.value.searchQuery != text) {
                    viewModel.handleEvent(ChannelListEvent.SearchQueryChanged(text))
                }
            }
        }
    }
}
package ru.snowadv.channels.presentation.channel_list

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.channels.databinding.FragmentChannelListBinding
import ru.snowadv.channels.presentation.channel_list.event.ChannelListEvent
import ru.snowadv.channels.presentation.channel_list.event.ChannelListFragmentEvent
import ru.snowadv.channels.presentation.channel_list.state.ChannelListScreenState
import ru.snowadv.channels.presentation.channel_list.view_model.ChannelListViewModel
import ru.snowadv.presentation.fragment.FragmentDataObserver

internal class ChannelListFragmentDataObserver :
    FragmentDataObserver<FragmentChannelListBinding, ChannelListViewModel, ChannelListFragment> {

    override fun ChannelListFragment.registerObservingFragment(
        binding: FragmentChannelListBinding,
        viewModel: ChannelListViewModel
    ) {
        observeState(binding, viewModel)
        observeEventFlow(viewModel)
        initListeners(binding, viewModel)
    }

    private fun ChannelListFragment.observeEventFlow(viewModel: ChannelListViewModel) {
        viewModel.eventFlow.onEach {
            handleFragmentEvent(it)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun ChannelListFragment.handleFragmentEvent(
        event: ChannelListFragmentEvent
    ) {
        when(event) {
            ChannelListFragmentEvent.ShowKeyboardAndFocusOnTextField -> showKeyboardAndFocusOnSearchField()
        }
    }

    private fun ChannelListFragment.observeState(
        binding: FragmentChannelListBinding,
        viewModel: ChannelListViewModel,
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
        viewModel: ChannelListViewModel,
    ) {
        binding.searchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let { text ->
                if (viewModel.state.value.searchQuery != text) {
                    viewModel.handleEvent(ChannelListEvent.SearchQueryChanged(text))
                }
            }
        }
        binding.searchBar.searchIcon.setOnClickListener {
            viewModel.handleEvent(ChannelListEvent.SearchIconClicked)
        }
    }
}
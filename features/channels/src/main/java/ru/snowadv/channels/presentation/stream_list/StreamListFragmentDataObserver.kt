package ru.snowadv.channels.presentation.stream_list

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.channels.databinding.FragmentStreamListBinding
import ru.snowadv.channels.presentation.adapter.StreamAdapterDelegate
import ru.snowadv.channels.presentation.adapter.TopicAdapterDelegate
import ru.snowadv.channels.presentation.stream_list.event.StreamListFragmentEvent
import ru.snowadv.channels.presentation.stream_list.state.StreamListScreenState
import ru.snowadv.channels.presentation.stream_list.event.StreamListEvent
import ru.snowadv.channels.presentation.stream_list.view_model.StreamListViewModel
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.setupDiffDelegatesAdapter
import ru.snowadv.presentation.adapter.updateAnimationDurations
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.recycler.setupDefaultDecorator
import ru.snowadv.presentation.view.setVisibility

internal class StreamListFragmentDataObserver :
    FragmentDataObserver<FragmentStreamListBinding, StreamListViewModel, StreamListFragment> {

    companion object {
        const val RECYCLER_ANIMATION_DURATION = 80L
    }

    override fun StreamListFragment.registerObservingFragment(
        binding: FragmentStreamListBinding,
        viewModel: StreamListViewModel
    ) {
        observeState(binding, viewModel, binding.streamsRecycler.setupAdapter(viewModel))
        observeEvents(binding, viewModel)
        binding.stateBox.setOnRetryClickListener {
            viewModel.handleEvent(StreamListEvent.ClickedOnReload)
        }
    }

    private fun StreamListFragment.observeState(
        binding: FragmentStreamListBinding,
        viewModel: StreamListViewModel,
        adapter: DiffDelegationAdapter
    ) {
        viewModel.state.onEach {
            bindState(binding, adapter, it)
        }.flowWithLifecycle(lifecycle).launchIn(lifecycleScope)
    }

    private fun StreamListFragment.observeEvents(
        binding: FragmentStreamListBinding,
        viewModel: StreamListViewModel,
    ) {
        viewModel.eventFlow.onEach {
            handleEvent(binding, it)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(lifecycleScope)
    }

    private fun StreamListFragment.handleEvent(
        binding: FragmentStreamListBinding,
        event: StreamListFragmentEvent
    ) = with(binding) {
        when (event) {
            is StreamListFragmentEvent.ShowInternetError -> {
                showInternetError(root)
            }

            is StreamListFragmentEvent.ShowInternetErrorWithRetry -> {
                showActionInternetErrorWithRetry(root, event.retryAction)
            }
        }
    }
    private fun bindState(
        binding: FragmentStreamListBinding,
        adapter: DiffDelegationAdapter,
        state: StreamListScreenState
    ) = with(binding) {
        actionLoadingBox.root.setVisibility(state.actionInProgress)
        val filteredState = state.filteredScreenState()
        stateBox.inflateState(state.screenState)
        adapter.submitList(filteredState.getCurrentData() ?: emptyList())
    }

    private fun RecyclerView.setupAdapter(viewModel: StreamListViewModel): DiffDelegationAdapter {
        setupDefaultDecorator()
        itemAnimator?.updateAnimationDurations(RECYCLER_ANIMATION_DURATION)
        return setupDelegatesAdapter(viewModel).also { adapter = it }
    }

    private fun setupDelegatesAdapter(viewModel: StreamListViewModel): DiffDelegationAdapter {
        return setupDiffDelegatesAdapter(
            setupStreamAdapterDelegate(viewModel),
            setupTopicAdapterDelegate(viewModel),
        )
    }

    private fun setupStreamAdapterDelegate(viewModel: StreamListViewModel): StreamAdapterDelegate {
        return StreamAdapterDelegate(
            onStreamClickListener = {
                viewModel.handleEvent(StreamListEvent.ClickedOnStream(it.id))
            }
        )
    }

    private fun setupTopicAdapterDelegate(viewModel: StreamListViewModel): TopicAdapterDelegate {
        return TopicAdapterDelegate(
            onTopicClickListener = {
                viewModel.handleEvent(StreamListEvent.ClickedOnTopic(it.name))
            }
        )
    }


}
package ru.snowadv.channels.presentation.stream_list

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.channels.R
import ru.snowadv.channels.databinding.FragmentStreamListBinding
import ru.snowadv.channels.presentation.adapter.ShimmerTopicAdapterDelegate
import ru.snowadv.channels.presentation.adapter.StreamAdapterDelegate
import ru.snowadv.channels.presentation.adapter.TopicAdapterDelegate
import ru.snowadv.channels.presentation.stream_list.elm.StreamListEffectElm
import ru.snowadv.channels.presentation.stream_list.elm.StreamListEventElm
import ru.snowadv.channels.presentation.stream_list.elm.StreamListStateElm
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.setupDiffDelegatesAdapter
import ru.snowadv.presentation.adapter.updateAnimationDurations
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.recycler.setupDefaultDecorator
import vivid.money.elmslie.core.store.Store

internal class StreamListRenderer :
    ElmFragmentRenderer<StreamListFragment, FragmentStreamListBinding, StreamListEventElm, StreamListEffectElm, StreamListStateElm> {

    companion object {
        const val RECYCLER_ANIMATION_DURATION = 80L
    }

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) {"Adapter wasn't initialized"}

    override fun StreamListFragment.onRendererViewCreated(
        binding: FragmentStreamListBinding,
        store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>,
    ) {
        binding.streamsRecycler.setupAdapter(store).also { _adapter = it }
        binding.stateBox.setOnRetryClickListener {
            store.accept(StreamListEventElm.Ui.ReloadClicked)
        }
        parentSearchQueryFlow.onEach {
            store.accept(StreamListEventElm.Ui.ChangedQuery(it))
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun StreamListFragment.renderStateByRenderer(
        state: StreamListStateElm,
        binding: FragmentStreamListBinding
    ) = with(binding) {
        stateBox.inflateState(screenState = state.screenState, shimmerLayout = R.layout.fragment_stream_list_shimmer)
        adapter.submitList(state.screenState.getCurrentData() ?: emptyList())
    }

    override fun StreamListFragment.handleEffectByRenderer(
        effect: StreamListEffectElm,
        binding: FragmentStreamListBinding,
        store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>
    ) = with(binding) {
        when (effect) {
            is StreamListEffectElm.ShowInternetErrorWithRetry -> {
                showActionInternetErrorWithRetry(root) { store.accept(effect.retryEvent) }
            }
        }
    }

    override fun StreamListFragment.onDestroyRendererView() {
        _adapter = null
    }

    private fun RecyclerView.setupAdapter(store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>): DiffDelegationAdapter {
        setupDefaultDecorator()
        itemAnimator?.updateAnimationDurations(RECYCLER_ANIMATION_DURATION)
        return setupDelegatesAdapter(store).also { adapter = it }
    }

    private fun setupDelegatesAdapter(store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>): DiffDelegationAdapter {
        return setupDiffDelegatesAdapter(
            setupStreamAdapterDelegate(store),
            setupTopicAdapterDelegate(store),
            setupShimmerTopicAdapterDelegate(),
        )
    }

    private fun setupStreamAdapterDelegate(store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>): StreamAdapterDelegate {
        return StreamAdapterDelegate(
            onStreamClickListener = {
                store.accept(StreamListEventElm.Ui.ClickedOnStream(it.id))
            }
        )
    }

    private fun setupTopicAdapterDelegate(store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>): TopicAdapterDelegate {
        return TopicAdapterDelegate(
            onTopicClickListener = {
                store.accept(StreamListEventElm.Ui.ClickedOnTopic(it.name))
            }
        )
    }

    private fun setupShimmerTopicAdapterDelegate(): ShimmerTopicAdapterDelegate {
        return ShimmerTopicAdapterDelegate()
    }


}
package ru.snowadv.channels_presentation.stream_list.ui

import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.channels_presentation.R
import ru.snowadv.channels_presentation.databinding.FragmentStreamListBinding
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationComponentHolder
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEffectElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListEventElm
import ru.snowadv.channels_presentation.stream_list.presentation.elm.StreamListStateElm
import ru.snowadv.channels_presentation.stream_list.ui.adapter.ShimmerTopicAdapterDelegate
import ru.snowadv.channels_presentation.stream_list.ui.adapter.StreamAdapterDelegate
import ru.snowadv.channels_presentation.stream_list.ui.adapter.TopicAdapterDelegate
import ru.snowadv.channels_presentation.stream_list.ui.elm.StreamListEffectUiElm
import ru.snowadv.channels_presentation.stream_list.ui.elm.StreamListEventUiElm
import ru.snowadv.channels_presentation.stream_list.ui.elm.StreamListStateUiElm
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.setupDiffDelegatesAdapter
import ru.snowadv.presentation.adapter.updateAnimationDurations
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.recycler.setupDecorator
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class StreamListRenderer :
    ElmFragmentRenderer<StreamListFragment, FragmentStreamListBinding, StreamListEventElm, StreamListEffectElm, StreamListStateElm> {

    companion object {
        const val RECYCLER_ANIMATION_DURATION = 80L
    }

    @Inject
    internal lateinit var mapper: ElmMapper<StreamListStateElm, StreamListEffectElm, StreamListEventElm, StreamListStateUiElm, StreamListEffectUiElm, StreamListEventUiElm>

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized" }

    override fun StreamListFragment.onAttachRendererView() {
        ChannelsPresentationComponentHolder.getComponent().inject(this@StreamListRenderer)
    }

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
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    override fun StreamListFragment.renderStateByRenderer(
        state: StreamListStateElm,
        binding: FragmentStreamListBinding
    ) = with(binding) {
        val mappedState = mapper.mapState(state)
        stateBox.inflateState(
            screenState = mappedState.screenState,
            shimmerLayout = R.layout.fragment_stream_list_shimmer,
            cacheStateBinding = topStateBox,
        )
        adapter.submitList(mappedState.screenState.getCurrentData() ?: emptyList())
    }

    override fun StreamListFragment.handleEffectByRenderer(
        effect: StreamListEffectElm,
        binding: FragmentStreamListBinding,
        store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>
    ) = with(binding) {
        when (val mappedEffect = mapper.mapEffect(effect)) {
            is StreamListEffectUiElm.ShowInternetErrorWithRetry -> {
                showActionInternetErrorWithRetry(root) { store.accept(mappedEffect.retryEvent) }
            }
        }
    }

    override fun StreamListFragment.onDestroyRendererView() {
        _adapter = null
    }

    private fun RecyclerView.setupAdapter(store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm>): DiffDelegationAdapter {
        setupDecorator(
            horizontalSpacingResId = ru.snowadv.presentation.R.dimen.small_common_padding,
            verticalSpacingResId = ru.snowadv.presentation.R.dimen.small_common_padding,
        )
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
            onExpandStreamClickListener = {
                store.accept(mapper.mapUiEvent(StreamListEventUiElm.ClickedOnExpandStream(it.id)))
            },
            onChangeStreamSubscriptionStatusClickListener = {
                store.accept(mapper.mapUiEvent(StreamListEventUiElm.ClickedOnChangeStreamSubscriptionStatus(it)))
            },
            onOpenStreamClickListener = {
                store.accept(mapper.mapUiEvent(StreamListEventUiElm.ClickedOnOpenStream(it.id, it.name)))
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
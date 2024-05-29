package ru.snowadv.channels_presentation.channel_list

import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.channels_presentation.R
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListEffectElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListEventElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListStateElm
import ru.snowadv.channels_presentation.databinding.FragmentChannelListBinding
import ru.snowadv.channels_presentation.stream_creator.StreamCreatorBottomSheetDialog
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.core.store.Store

internal class ChannelListFragmentRenderer :
    ElmFragmentRenderer<ChannelListFragment, FragmentChannelListBinding, ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm> {

    companion object {
        const val STREAM_CREATOR_REQUEST_KEY = "stream_creator_request"
    }

    override fun ChannelListFragment.onRendererViewCreated(
        binding: FragmentChannelListBinding,
        store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>,
    ) {
        initListeners(binding, store)
    }

    override fun ChannelListFragment.renderStateByRenderer(
        state: ChannelListStateElm,
        binding: FragmentChannelListBinding
    ) = with(binding) {
        mutableStateFlow.tryEmit(state.searchQuery)
        if (channelsSearchBar.searchEditText.text.toString() != state.searchQuery) {
            channelsSearchBar.searchEditText.setText(state.searchQuery)
        }
    }

    override fun ChannelListFragment.handleEffectByRenderer(
        effect: ChannelListEffectElm,
        binding: FragmentChannelListBinding,
        store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>
    ) {
        when (effect) {
            ChannelListEffectElm.ShowKeyboardAndFocusOnTextField -> showKeyboardAndFocusOnSearchField()
            ChannelListEffectElm.OpenStreamCreator -> openStreamCreator(this)
            is ChannelListEffectElm.ShowNewStreamCreated -> {
                showInfo(
                    rootView = binding.root,
                    text = String.format(getString(R.string.new_stream_created), effect.name),
                )
            }
        }
    }

    private fun ChannelListFragment.initListeners(
        binding: FragmentChannelListBinding,
        store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>,
    ) {
        binding.channelsSearchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let { text ->
                mutableStateFlow.tryEmit(text)
            }
        }
        binding.channelsSearchBar.searchIcon.setOnClickListener {
            store.accept(ChannelListEventElm.Ui.SearchIconClicked)
        }
        searchQuery.onEach { store.accept(ChannelListEventElm.Ui.ChangedSearchQuery(it)) }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.createStreamFloatingButton.setOnClickListener {
            store.accept(ChannelListEventElm.Ui.CreateNewStreamClicked)
        }
        initStreamCreatorResultListener(this, store)
    }

    private fun initStreamCreatorResultListener(
        fragment: Fragment,
        store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>,
    ) {
        fragment.childFragmentManager.setFragmentResultListener(
            STREAM_CREATOR_REQUEST_KEY,
            fragment.viewLifecycleOwner
        ) { _, bundle ->
            bundle.getString(StreamCreatorBottomSheetDialog.BUNDLE_CREATED_STREAM_NAME)?.let { newStream ->
                store.accept(ChannelListEventElm.Ui.NewStreamCreated(newStream))
            }
        }
    }

    private fun openStreamCreator(fragment: Fragment) {
        StreamCreatorBottomSheetDialog.newInstance(STREAM_CREATOR_REQUEST_KEY)
            .show(fragment.childFragmentManager)
    }
}
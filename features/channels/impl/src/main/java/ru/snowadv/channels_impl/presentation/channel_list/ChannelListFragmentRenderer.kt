package ru.snowadv.channels_impl.presentation.channel_list

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.snowadv.channels_impl.databinding.FragmentChannelListBinding
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListEffectElm
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListEventElm
import ru.snowadv.channels_impl.presentation.channel_list.elm.ChannelListStateElm
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.core.store.Store

internal class ChannelListFragmentRenderer :
    ElmFragmentRenderer<ChannelListFragment, FragmentChannelListBinding, ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm> {

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
        if (searchBar.searchEditText.text.toString() != state.searchQuery) {
            searchBar.searchEditText.setText(state.searchQuery)
        }
    }

    override fun ChannelListFragment.handleEffectByRenderer(
        effect: ChannelListEffectElm,
        binding: FragmentChannelListBinding,
        store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>
    ) {
        when(effect) {
            ChannelListEffectElm.ShowKeyboardAndFocusOnTextField -> showKeyboardAndFocusOnSearchField()
        }
    }

    private fun ChannelListFragment.initListeners(
        binding: FragmentChannelListBinding,
        store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>,
    ) {
        binding.searchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let { text ->
                mutableStateFlow.tryEmit(text)
            }
        }
        binding.searchBar.searchIcon.setOnClickListener {
            store.accept(ChannelListEventElm.Ui.SearchIconClicked)
        }
        searchQuery.onEach { store.accept(ChannelListEventElm.Ui.ChangedSearchQuery(it)) }
            .flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(viewLifecycleOwner.lifecycleScope)
    }
}
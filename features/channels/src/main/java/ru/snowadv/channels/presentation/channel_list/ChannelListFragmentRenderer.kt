package ru.snowadv.channels.presentation.channel_list

import androidx.core.widget.addTextChangedListener
import ru.snowadv.channels.databinding.FragmentChannelListBinding
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListEffectElm
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListEventElm
import ru.snowadv.channels.presentation.channel_list.elm.ChannelListStateElm
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

    private fun render(
        binding: FragmentChannelListBinding,
        query: String,
    ) = with(binding) {
        if (searchBar.searchEditText.text.toString() != query) {
            searchBar.searchEditText.setText(query)
        }
    }

    private fun initListeners(
        binding: FragmentChannelListBinding,
        store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>,
    ) {
        binding.searchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let { text ->
                store.accept(ChannelListEventElm.Ui.ChangedSearchQuery(text))
            }
        }
        binding.searchBar.searchIcon.setOnClickListener {
            store.accept(ChannelListEventElm.Ui.SearchIconClicked)
        }
    }
}
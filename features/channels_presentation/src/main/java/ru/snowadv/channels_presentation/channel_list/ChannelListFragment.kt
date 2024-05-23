package ru.snowadv.channels_presentation.channel_list

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import ru.snowadv.channels_presentation.databinding.FragmentChannelListBinding
import ru.snowadv.channels_domain_api.model.StreamType
import ru.snowadv.channels_presentation.channel_list.api.SearchHolder
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListEffectElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListEventElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListStateElm
import ru.snowadv.channels_presentation.channel_list.elm.ChannelListStoreFactoryElm
import ru.snowadv.channels_presentation.channel_list.pager_adapter.StreamsAdapter
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationComponentHolder
import ru.snowadv.channels_presentation.stream_list.ui.util.StreamsMapper.toLocalizedString
import ru.snowadv.presentation.activity.showKeyboard
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.PopupHandlingFragment
import ru.snowadv.presentation.fragment.impl.SnackbarPopupHandlingFragment
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChannelListFragment : BaseFragment<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>(),
    PopupHandlingFragment by SnackbarPopupHandlingFragment(),
    ElmFragmentRenderer<ChannelListFragment, FragmentChannelListBinding, ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm>
    by ChannelListFragmentRenderer(), SearchHolder {



    companion object {
        const val DEBOUNCE_SEARCH_QUERY_MILLIS = 200L
        fun newInstance(): Fragment = ChannelListFragment()
    }

    private var _binding: FragmentChannelListBinding? = null
    private val binding get() = _binding!!

    @Inject
    internal lateinit var channelListStoreFactoryElm: ChannelListStoreFactoryElm
    val mutableStateFlow = MutableStateFlow("")
    override val searchQuery: Flow<String> = mutableStateFlow.debounce(DEBOUNCE_SEARCH_QUERY_MILLIS)

    override val store: Store<ChannelListEventElm, ChannelListEffectElm, ChannelListStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        channelListStoreFactoryElm.create()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ChannelsPresentationComponentHolder.getComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentChannelListBinding.inflate(inflater).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPagerAdapter()
        setupPagerTabLayoutMediator()
        onRendererViewCreated(binding, store)
    }

    override fun render(state: ChannelListStateElm) {
        renderStateByRenderer(state, binding)
    }

    override fun handleEffect(effect: ChannelListEffectElm) {
        handleEffectByRenderer(effect, binding, store)
    }

    override fun onDestroyView() {
        onDestroyRendererView()
        _binding = null
        super.onDestroyView()
    }

    fun showKeyboardAndFocusOnSearchField() {
        binding.channelsSearchBar.searchEditText.requestFocus()
        activity?.showKeyboard(binding.channelsSearchBar.searchEditText)
    }

    private fun setupPagerAdapter() {
        binding.streamTypesPager.adapter = StreamsAdapter(childFragmentManager, lifecycle)
    }

    private fun setupPagerTabLayoutMediator() {
        TabLayoutMediator(binding.tabsStreamTypes, binding.streamTypesPager) { tab, position ->
            tab.text = StreamType.entries[position].toLocalizedString(requireContext())
        }.attach()
    }
}
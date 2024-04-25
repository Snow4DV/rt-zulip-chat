package ru.snowadv.channels.presentation.channel_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.snowadv.channels.databinding.FragmentChannelListBinding
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.presentation.channel_list.api.SearchHolder
import ru.snowadv.channels.presentation.channel_list.pager_adapter.StreamsAdapter
import ru.snowadv.channels.presentation.channel_list.view_model.ChannelListViewModel
import ru.snowadv.channels.presentation.util.ChannelsMapper.toLocalizedString
import ru.snowadv.presentation.activity.showKeyboard
import ru.snowadv.presentation.fragment.ErrorHandlingFragment
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.impl.SnackbarErrorHandlingFragment

class ChannelListFragment : Fragment(), ErrorHandlingFragment by SnackbarErrorHandlingFragment(),
    FragmentDataObserver<FragmentChannelListBinding, ChannelListViewModel, ChannelListFragment> by ChannelListFragmentDataObserver(),
    SearchHolder {



    companion object {
        fun newInstance(): Fragment = ChannelListFragment()
    }

    private var _binding: FragmentChannelListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChannelListViewModel by viewModels()

    override val searchQuery: StateFlow<String>
        get() = viewModel.searchQueryPublisher

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChannelListBinding.inflate(inflater).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupPagerAdapter()
        setupPagerTabLayoutMediator()
        registerObservingFragment(binding, viewModel)
    }

    override fun onDestroyView() {
        onViewDestroyedToObserver()
        _binding = null
        super.onDestroyView()
    }

    fun showKeyboardAndFocusOnSearchField() {
        binding.searchBar.searchEditText.requestFocus()
        activity?.showKeyboard(binding.searchBar.searchEditText)
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
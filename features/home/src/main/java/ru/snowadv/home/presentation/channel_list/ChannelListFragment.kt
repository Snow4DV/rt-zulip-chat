package ru.snowadv.home.presentation.channel_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.tabs.TabLayoutMediator
import ru.snowadv.home.databinding.FragmentChannelListBinding
import ru.snowadv.home.domain.model.StreamType
import ru.snowadv.home.presentation.channel_list.pager_adapter.StreamsAdapter
import ru.snowadv.home.presentation.channel_list.view_model.ChannelListSharedViewModel
import ru.snowadv.home.presentation.channel_list.view_model.ChannelListViewModelFactory
import ru.snowadv.home.presentation.di.HomeGraph
import ru.snowadv.home.presentation.navigation.HomeRouter
import ru.snowadv.home.presentation.util.toLocalizedString
import ru.snowadv.presentation.fragment.ErrorHandlingFragment
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.impl.SnackbarErrorHandlingFragment

class ChannelListFragment : Fragment(), ErrorHandlingFragment by SnackbarErrorHandlingFragment(),
    FragmentDataObserver<FragmentChannelListBinding, ChannelListSharedViewModel, ChannelListFragment> by ChannelListFragmentDataObserver(){

    companion object {
        fun newInstance() = ChannelListFragment()
    }

    private var _binding: FragmentChannelListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChannelListSharedViewModel by viewModels(
        factoryProducer = {
            ChannelListViewModelFactory(HomeGraph.router)
        }
    )

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

    private fun setupPagerAdapter() {
        binding.streamTypesPager.adapter = StreamsAdapter(childFragmentManager, lifecycle)
    }

    private fun setupPagerTabLayoutMediator() {
        TabLayoutMediator(binding.tabsStreamTypes, binding.streamTypesPager) { tab, position ->
            tab.text = StreamType.entries[position].toLocalizedString(requireContext())
        }.attach()
    }
}
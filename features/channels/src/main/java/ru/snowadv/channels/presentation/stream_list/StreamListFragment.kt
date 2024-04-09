package ru.snowadv.channels.presentation.stream_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import ru.snowadv.channels.databinding.FragmentStreamListBinding
import ru.snowadv.channels.di.ChannelsGraph
import ru.snowadv.channels.domain.model.StreamType
import ru.snowadv.channels.presentation.channel_list.api.SearchHolder
import ru.snowadv.channels.presentation.stream_list.view_model.StreamListViewModel
import ru.snowadv.channels.presentation.stream_list.view_model.StreamListViewModelFactory
import ru.snowadv.presentation.fragment.ErrorHandlingFragment
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.impl.SnackbarErrorHandlingFragment

class StreamListFragment : Fragment(), ErrorHandlingFragment by SnackbarErrorHandlingFragment(),
    FragmentDataObserver<FragmentStreamListBinding, StreamListViewModel, StreamListFragment> by StreamListFragmentDataObserver() {

    companion object {
        const val ARG_STREAMS_TYPE_KEY = "arg_streams_type"
        const val DEFAULT_STREAM_TYPE = "ALL"
        fun newInstance(streamsType: StreamType) = StreamListFragment().apply {
            arguments = bundleOf(
                ARG_STREAMS_TYPE_KEY to streamsType.toString()
            )
        }
    }

    private var _binding: FragmentStreamListBinding? = null
    private val binding get() = requireNotNull(_binding) {"Binding wasn't initialized"}
    private val parentSearchQueryFlow get() = (requireParentFragment() as SearchHolder).searchQuery


    private val viewModel: StreamListViewModel by viewModels {
        StreamListViewModelFactory(
            streamType = streamsType,
            router = ChannelsGraph.deps.router,
            searchQueryFlow = parentSearchQueryFlow,
        )
    }

    private val streamsType: StreamType by lazy {
        StreamType.valueOf(
            arguments?.getString(
                ARG_STREAMS_TYPE_KEY,
                DEFAULT_STREAM_TYPE
            ) ?: DEFAULT_STREAM_TYPE
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentStreamListBinding.inflate(inflater).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel)
    }

    override fun onDestroyView() {
        onViewDestroyedToObserver()
        _binding = null
        super.onDestroyView()
    }
}
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
import ru.snowadv.channels.presentation.stream_list.elm.StreamListEffectElm
import ru.snowadv.channels.presentation.stream_list.elm.StreamListEventElm
import ru.snowadv.channels.presentation.stream_list.elm.StreamListStateElm
import ru.snowadv.channels.presentation.stream_list.elm.StreamListStoreFactoryElm
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.ErrorHandlingFragment
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.impl.SnackbarErrorHandlingFragment
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store

internal class StreamListFragment : BaseFragment<StreamListEventElm, StreamListEffectElm, StreamListStateElm>(),
    ErrorHandlingFragment by SnackbarErrorHandlingFragment(),
    ElmFragmentRenderer<StreamListFragment, FragmentStreamListBinding, StreamListEventElm, StreamListEffectElm, StreamListStateElm>
    by StreamListRenderer() {

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
    val parentSearchQueryFlow get() = (requireParentFragment() as SearchHolder).searchQuery

    private val streamsType: StreamType by lazy {
        StreamType.valueOf(
            arguments?.getString(
                ARG_STREAMS_TYPE_KEY,
                DEFAULT_STREAM_TYPE
            ) ?: DEFAULT_STREAM_TYPE
        )
    }
    override val store: Store<StreamListEventElm, StreamListEffectElm, StreamListStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        StreamListStoreFactoryElm(
            actor = ChannelsGraph.streamListActorElm,
            streamType = streamsType,
        ).create()
    }
    override val resumeUiEvent: StreamListEventElm = StreamListEventElm.Ui.Resumed
    override val pauseUiEvent: StreamListEventElm = StreamListEventElm.Ui.Paused

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentStreamListBinding.inflate(inflater).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
    }

    override fun render(state: StreamListStateElm) {
        renderStateByRenderer(state, binding)
    }

    override fun handleEffect(effect: StreamListEffectElm) {
        handleEffectByRenderer(effect, binding, store)
    }

    override fun onDestroyView() {
        onDestroyRendererView()
        _binding = null
        super.onDestroyView()
    }
}
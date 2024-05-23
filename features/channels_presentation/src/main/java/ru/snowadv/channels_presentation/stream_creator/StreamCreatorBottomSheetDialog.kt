package ru.snowadv.channels_presentation.stream_creator

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.setFragmentResult
import ru.snowadv.channels_presentation.databinding.FragmentStreamCreatorBinding
import ru.snowadv.channels_presentation.di.holder.ChannelsPresentationComponentHolder
import ru.snowadv.channels_presentation.stream_creator.elm.StreamCreatorEffectElm
import ru.snowadv.channels_presentation.stream_creator.elm.StreamCreatorEventElm
import ru.snowadv.channels_presentation.stream_creator.elm.StreamCreatorStateElm
import ru.snowadv.channels_presentation.stream_creator.elm.StreamCreatorStoreFactoryElm
import ru.snowadv.presentation.elm.BaseBottomSheetDialogFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.PopupHandlingFragment
import ru.snowadv.presentation.fragment.impl.SnackbarPopupHandlingFragment
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class StreamCreatorBottomSheetDialog : BaseBottomSheetDialogFragment<StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm>(),
    ElmFragmentRenderer<StreamCreatorBottomSheetDialog, FragmentStreamCreatorBinding, StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm>
    by StreamCreatorRenderer(), PopupHandlingFragment by SnackbarPopupHandlingFragment() {

    companion object {
        const val TAG = "stream_creator_dialog"

        const val DEFAULT_ARG_RESULT_KEY = "stream_creator_result"

        const val ARG_RESULT_KEY = "arg_result_key"

        const val BUNDLE_CREATED_STREAM_NAME = "created_stream_name"
        fun newInstance(resultKey: String) =
            StreamCreatorBottomSheetDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                )
            }
    }

    override val store: Store<StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        streamCreatorStoreFactoryElm.create()
    }

    @Inject
    internal lateinit var streamCreatorStoreFactoryElm: StreamCreatorStoreFactoryElm
    private var _binding: FragmentStreamCreatorBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }
    private val resultKey: String by lazy {
        requireArguments().getString(
            ARG_RESULT_KEY,
            DEFAULT_ARG_RESULT_KEY
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ChannelsPresentationComponentHolder.getComponent().inject(this)
        onAttachRendererView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStreamCreatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyRendererView()
        _binding = null
    }

    override fun render(state: StreamCreatorStateElm) {
        renderStateByRenderer(state = state, binding = binding)
    }

    override fun handleEffect(effect: StreamCreatorEffectElm) {
        handleEffectByRenderer(effect = effect, binding = binding, store = store)
    }

    fun finishWithNewStreamName(streamName: String) {
        setFragmentResult(
            resultKey, bundleOf(
                BUNDLE_CREATED_STREAM_NAME to streamName,
            )
        )
        dismissAllowingStateLoss()
    }

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)
}
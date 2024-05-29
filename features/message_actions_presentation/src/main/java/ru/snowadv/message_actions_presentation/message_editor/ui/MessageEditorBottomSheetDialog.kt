package ru.snowadv.message_actions_presentation.message_editor.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import ru.snowadv.message_actions_presentation.api.model.MessageEditorResult
import ru.snowadv.message_actions_presentation.api.screen_factory.ActionChooserDialogFactory.Companion.BUNDLE_RESULT_KEY
import ru.snowadv.message_actions_presentation.databinding.FragmentMessageEditorBinding
import ru.snowadv.message_actions_presentation.di.dagger.MessageActionsPresentationComponentHolder
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEffectElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEventElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorStateElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorStoreFactoryElm
import ru.snowadv.presentation.elm.BaseBottomSheetDialogFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class MessageEditorBottomSheetDialog : BaseBottomSheetDialogFragment<MessageEditorEventElm, MessageEditorEffectElm, MessageEditorStateElm>(),
    ElmFragmentRenderer<MessageEditorBottomSheetDialog, FragmentMessageEditorBinding, MessageEditorEventElm, MessageEditorEffectElm, MessageEditorStateElm>
    by MessageEditorRenderer() {

    companion object {
        const val DEFAULT_ARG_RESULT_KEY = "message_editor_result"
        const val DEFAULT_ARG_MESSAGE_ID = -1L
        const val DEFAULT_ARG_USER_ID = -1L

        const val ARG_RESULT_KEY = "arg_result_key"
        const val ARG_MESSAGE_ID_KEY = "message_id"
        const val ARG_STREAM_NAME_KEY = "stream_name"
        fun newInstance(resultKey: String, messageId: Long, streamName: String) =
            MessageEditorBottomSheetDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    ARG_MESSAGE_ID_KEY to messageId,
                    ARG_STREAM_NAME_KEY to streamName,
                )
            }
    }

    @Inject
    internal lateinit var messageEditorStoreFactoryElm: MessageEditorStoreFactoryElm

    private val resultKey: String by lazy {
        requireArguments().getString(
            ARG_RESULT_KEY,
            DEFAULT_ARG_RESULT_KEY
        )
    }
    private val messageId: Long by lazy {
        requireArguments().getLong(ARG_MESSAGE_ID_KEY, DEFAULT_ARG_MESSAGE_ID)
            .also {
                if (it == DEFAULT_ARG_MESSAGE_ID) error("Missing message id argument")
            }
    }
    private val streamName: String by lazy {
        requireNotNull(requireArguments().getString(ARG_STREAM_NAME_KEY, null)) {
            "Missing stream name argument"
        }
    }

    private var _binding: FragmentMessageEditorBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }

    override val store: Store<MessageEditorEventElm, MessageEditorEffectElm, MessageEditorStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        messageEditorStoreFactoryElm.create(messageId = messageId, streamName = streamName)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MessageActionsPresentationComponentHolder.getComponent().inject(this)
        onAttachRendererView()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageEditorBinding.inflate(inflater, container, false)
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

    override fun render(state: MessageEditorStateElm) {
        renderStateByRenderer(state = state, binding = binding)
    }

    override fun handleEffect(effect: MessageEditorEffectElm) {
        handleEffectByRenderer(effect = effect, binding = binding, store = store)
    }

    fun finishWithResult(result: MessageEditorResult) {
        setFragmentResult(
            resultKey, bundleOf(
                BUNDLE_RESULT_KEY to result,
            )
        )
        this.dismissAllowingStateLoss()
    }
}
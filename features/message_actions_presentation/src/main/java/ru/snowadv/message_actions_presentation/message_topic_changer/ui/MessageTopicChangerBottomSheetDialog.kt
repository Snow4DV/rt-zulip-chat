package ru.snowadv.message_actions_presentation.message_topic_changer.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import ru.snowadv.message_actions_presentation.api.model.MessageMoveResult
import ru.snowadv.message_actions_presentation.api.screen_factory.ActionChooserDialogFactory.Companion.BUNDLE_RESULT_KEY
import ru.snowadv.message_actions_presentation.databinding.FragmentTopicChangerBinding
import ru.snowadv.message_actions_presentation.di.dagger.MessageActionsPresentationComponentHolder
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerEffectElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerEventElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerStateElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerStoreFactoryElm
import ru.snowadv.presentation.elm.BaseBottomSheetDialogFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class MessageTopicChangerBottomSheetDialog : BaseBottomSheetDialogFragment<MessageTopicChangerEventElm, MessageTopicChangerEffectElm, MessageTopicChangerStateElm>(),
    ElmFragmentRenderer<MessageTopicChangerBottomSheetDialog, FragmentTopicChangerBinding, MessageTopicChangerEventElm, MessageTopicChangerEffectElm, MessageTopicChangerStateElm>
    by MessageTopicChangerRenderer() {

    companion object {
        const val DEFAULT_ARG_RESULT_KEY = "message_topic_changer_result"
        const val DEFAULT_ARG_MESSAGE_ID = -1L
        const val DEFAULT_ARG_STREAM_ID = -1L

        const val ARG_RESULT_KEY = "arg_result_key"
        const val ARG_MESSAGE_ID_KEY = "message_id"
        const val ARG_STREAM_ID_KEY = "stream_id"
        const val ARG_TOPIC_NAME_KEY = "topic_name"
        fun newInstance(resultKey: String, messageId: Long, streamId: Long, topicName: String) =
            MessageTopicChangerBottomSheetDialog().apply {
                arguments = bundleOf(
                    ARG_RESULT_KEY to resultKey,
                    ARG_MESSAGE_ID_KEY to messageId,
                    ARG_STREAM_ID_KEY to streamId,
                    ARG_TOPIC_NAME_KEY to topicName,
                )
            }
    }

    @Inject
    internal lateinit var messageTopicStoreFactoryElm: MessageTopicChangerStoreFactoryElm

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
    private val streamId: Long by lazy {
        requireArguments().getLong(ARG_STREAM_ID_KEY, DEFAULT_ARG_STREAM_ID)
            .also {
                if (it == DEFAULT_ARG_STREAM_ID) error("Missing stream id argument")
            }
    }
    private val topicName: String by lazy {
        requireNotNull(requireArguments().getString(ARG_TOPIC_NAME_KEY, null)) {
            "Missing topic name argument"
        }
    }

    private var _binding: FragmentTopicChangerBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }

    override val store: Store<MessageTopicChangerEventElm, MessageTopicChangerEffectElm, MessageTopicChangerStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        messageTopicStoreFactoryElm.create(messageId = messageId, streamId = streamId, topicName = topicName)
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
        _binding = FragmentTopicChangerBinding.inflate(inflater, container, false)
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

    override fun render(state: MessageTopicChangerStateElm) {
        renderStateByRenderer(state = state, binding = binding)
    }

    override fun handleEffect(effect: MessageTopicChangerEffectElm) {
        handleEffectByRenderer(effect = effect, binding = binding, store = store)
    }

    fun finishWithResult(result: MessageMoveResult) {
        setFragmentResult(
            resultKey, bundleOf(
                BUNDLE_RESULT_KEY to result,
            )
        )
        this.dismissAllowingStateLoss()
    }
}
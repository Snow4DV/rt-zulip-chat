package ru.snowadv.chat_presentation.chat.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEffectElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStoreFactoryElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEffectUiElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEventUiElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatStateUiElm
import ru.snowadv.chat_presentation.databinding.FragmentChatBinding
import ru.snowadv.chat_presentation.di.holder.ChatPresentationComponentHolder
import ru.snowadv.chat_presentation.chat.ui.model.ChatAction
import ru.snowadv.presentation.R
import ru.snowadv.presentation.adapter.util.PaddingItemDecorator
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.PopupHandlingFragment
import ru.snowadv.presentation.fragment.ResultUtils
import ru.snowadv.presentation.fragment.impl.SnackbarPopupHandlingFragment
import ru.snowadv.presentation.fragment.setStatusBarColor
import ru.snowadv.presentation.fragment.setTopBarColor
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class ChatFragment : BaseFragment<ChatEventElm, ChatEffectElm, ChatStateElm>(),
    ElmFragmentRenderer<ChatFragment, FragmentChatBinding, ChatEventElm, ChatEffectElm, ChatStateElm>
    by ChatFragmentRenderer(), PopupHandlingFragment by SnackbarPopupHandlingFragment() {

    private val fileChooser = ResultUtils.registerChooserUriLauncher(
        fragment = this,
        onSuccess = { mimeType, opener, extension ->
            store.accept(mapper.mapUiEvent(ChatEventUiElm.FileWasChosen(mimeType, opener, extension)))
        },
        onFailure = {
            store.accept(mapper.mapUiEvent(ChatEventUiElm.FileChoosingDismissed))
        },
    )

    companion object {
        const val ATTACHMENT_MIME_TYPE = "*/*"

        const val ARG_STREAM_ID_KEY = "stream_id"
        const val ARG_STREAM_NAME_KEY = "stream_name"
        const val ARG_TOPIC_NAME_KEY = "topic_name"

        const val DEFAULT_STREAM_ID = -1L
        fun newInstance(streamId: Long, streamName: String, topicName: String?): Fragment = ChatFragment().apply {
            arguments = bundleOf(
                ARG_STREAM_ID_KEY to streamId,
                ARG_STREAM_NAME_KEY to streamName,
                ARG_TOPIC_NAME_KEY to topicName,
            )
        }
    }

    @Inject
    internal lateinit var mapper: ElmMapper<ChatStateElm, ChatEffectElm, ChatEventElm, ChatStateUiElm, ChatEffectUiElm, ChatEventUiElm>
    @Inject
    internal lateinit var chatStoreFactory: ChatStoreFactoryElm

    private var _binding: FragmentChatBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }
    private val streamName: String by lazy {
        requireArguments().getString(ARG_STREAM_NAME_KEY) ?: error("Missing stream name argument")
    }
    private val streamId: Long by lazy {
        requireArguments().getLong(ARG_STREAM_ID_KEY, DEFAULT_STREAM_ID).also {
            if (it == DEFAULT_STREAM_ID) error("Missing stream id argument")
        }
    }
    private val topicName: String? by lazy {
        requireArguments().getString(ARG_TOPIC_NAME_KEY)
    }

    override val store: Store<ChatEventElm, ChatEffectElm, ChatStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        chatStoreFactory.create(
            streamName = streamName,
            topicName = topicName,
            streamId = streamId,
        )
    }
    override val resumeUiEvent: ChatEventElm = ChatEventElm.Ui.Resumed
    override val pauseUiEvent: ChatEventElm = ChatEventElm.Ui.Paused

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentChatBinding.inflate(layoutInflater).also {
            _binding = it
            addDecoratorToRecycler(it)
        }.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ChatPresentationComponentHolder.getComponent().inject(this)
        onAttachRendererView()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStatusBarColor(R.color.primary)
        onRendererViewCreated(binding, store)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyRendererView()
        _binding = null
    }

    override fun render(state: ChatStateElm) {
        renderStateByRenderer(state, binding)
    }

    override fun handleEffect(effect: ChatEffectElm) {
        handleEffectByRenderer(effect, binding, store)
    }

    private fun addDecoratorToRecycler(binding: FragmentChatBinding) {
        binding.messagesRecycler.addItemDecoration(initDecorator())
    }

    private fun initDecorator(): RecyclerView.ItemDecoration {
        return PaddingItemDecorator(
            requireContext(),
            R.dimen.small_common_padding,
            R.dimen.small_common_padding
        )
    }

    override fun onResume() {
        super.onResume()
        setStatusBarColor(R.color.primary)
    }

    override fun onPause() {
        setStatusBarColor(R.color.on_surface)
        super.onPause()
    }

    fun openActionsDialog(actions: List<ChatAction>, onResult: (ChatAction) -> Unit) {
        AlertDialog.Builder(requireContext())
            .setItems(actions.map { getString(it.titleResId) }.toTypedArray()) { _, which ->
                onResult(actions[which])
            }
            .create().show()
    }

    fun openFilePicker() {
        Intent(Intent.ACTION_GET_CONTENT).apply { type = ATTACHMENT_MIME_TYPE }.let { intent ->
            fileChooser.launch(intent)
        }
    }
}
package ru.snowadv.chat.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.di.ChatGraph
import ru.snowadv.chat.presentation.chat.elm.ChatEffectElm
import ru.snowadv.chat.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat.presentation.chat.elm.ChatStateElm
import ru.snowadv.chat.presentation.chat.elm.ChatStoreFactoryElm
import ru.snowadv.chat.presentation.model.ChatAction
import ru.snowadv.presentation.R
import ru.snowadv.presentation.adapter.util.PaddingItemDecorator
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.ErrorHandlingFragment
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.impl.SnackbarErrorHandlingFragment
import ru.snowadv.presentation.fragment.setStatusBarColor
import ru.snowadv.presentation.fragment.setTopBarColor
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store

internal class ChatFragment : BaseFragment<ChatEventElm, ChatEffectElm, ChatStateElm>(),
    ElmFragmentRenderer<ChatFragment, FragmentChatBinding, ChatEventElm, ChatEffectElm, ChatStateElm>
    by ChatFragmentRenderer(),
    ErrorHandlingFragment by SnackbarErrorHandlingFragment() {

    companion object {
        const val ARG_STREAM_NAME_KEY = "stream_name"
        const val ARG_TOPIC_NAME_KEY = "topic_name"
        fun newInstance(streamName: String, topicName: String): Fragment = ChatFragment().apply {
            arguments = bundleOf(
                ARG_STREAM_NAME_KEY to streamName,
                ARG_TOPIC_NAME_KEY to topicName,
            )
        }
    }

    private var _binding: FragmentChatBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }
    private val streamName: String by lazy {
        requireArguments().getString(ARG_STREAM_NAME_KEY) ?: error("Missing stream name argument")
    }
    private val topicName: String by lazy {
        requireArguments().getString(ARG_TOPIC_NAME_KEY) ?: error("Missing topic name argument")
    }

    override val store: Store<ChatEventElm, ChatEffectElm, ChatStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        ChatStoreFactoryElm(
            actor = ChatGraph.chatActorElm,
            streamName = streamName,
            topicName = topicName,
        ).create()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setStatusBarColor(R.color.primary)
        binding.topBackButtonBar.setTopBarColor(R.color.primary)
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

    fun openActionsDialog(actions: List<ChatAction>, onResult: (ChatAction) -> Unit) {
        AlertDialog.Builder(requireContext())
            .setItems(actions.map { getString(it.titleResId) }.toTypedArray()) { _, which ->
                onResult(actions[which])
            }
            .create().show()
    }
}
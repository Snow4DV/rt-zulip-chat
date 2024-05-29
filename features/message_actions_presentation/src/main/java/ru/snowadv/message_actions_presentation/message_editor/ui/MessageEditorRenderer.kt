package ru.snowadv.message_actions_presentation.message_editor.ui

import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.message_actions_presentation.R
import ru.snowadv.message_actions_presentation.api.model.MessageEditorResult
import ru.snowadv.message_actions_presentation.databinding.FragmentMessageEditorBinding
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEffectElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEventElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorStateElm
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.view.EditTextUtils.observe
import ru.snowadv.presentation.view.setTextIfEmpty
import vivid.money.elmslie.core.store.Store

internal class MessageEditorRenderer :
    ElmFragmentRenderer<MessageEditorBottomSheetDialog, FragmentMessageEditorBinding, MessageEditorEventElm, MessageEditorEffectElm, MessageEditorStateElm> {

    override fun MessageEditorBottomSheetDialog.onRendererViewCreated(
        binding: FragmentMessageEditorBinding,
        store: Store<MessageEditorEventElm, MessageEditorEffectElm, MessageEditorStateElm>
    ) {
        initListeners(binding, store)
    }

    override fun MessageEditorBottomSheetDialog.renderStateByRenderer(
        state: MessageEditorStateElm,
        binding: FragmentMessageEditorBinding
    ) {
        binding.messageContentEditText.setTextIfEmpty(state.editorState.data ?: "")
        binding.stateBox.inflateState(state.editorState, R.layout.fragment_message_editor_shimmer)
        binding.editMessageButton.isVisible = !state.editingMessage
        binding.editingMessageProgressBar.isVisible = state.editingMessage
    }

    override fun MessageEditorBottomSheetDialog.handleEffectByRenderer(
        effect: MessageEditorEffectElm,
        binding: FragmentMessageEditorBinding,
        store: Store<MessageEditorEventElm, MessageEditorEffectElm, MessageEditorStateElm>
    ) {
        when (effect) {
            is MessageEditorEffectElm.CloseWithResult -> finishWithResult(effect.result)
            is MessageEditorEffectElm.FinishWithError -> finishWithResult(
                MessageEditorResult.Error(effect.errorMessage ?: getString(R.string.error_has_occurred_please_try_again))
            )
        }
    }

    private fun MessageEditorBottomSheetDialog.initListeners(
        binding: FragmentMessageEditorBinding,
        store: Store<MessageEditorEventElm, MessageEditorEffectElm, MessageEditorStateElm>,
    ) {
        binding.stateBox.setOnRetryClickListener {
            store.accept(MessageEditorEventElm.Ui.OnRetryClicked)
        }
        binding.messageContentEditText.observe().onEach {
            store.accept(MessageEditorEventElm.Ui.ChangedContentText(it))
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
        binding.editMessageButton.setOnClickListener {
            store.accept(MessageEditorEventElm.Ui.OnApplyClicked)
        }
    }
}
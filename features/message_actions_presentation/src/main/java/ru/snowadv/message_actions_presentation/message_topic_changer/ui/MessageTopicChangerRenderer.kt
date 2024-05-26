package ru.snowadv.message_actions_presentation.message_topic_changer.ui

import android.widget.ArrayAdapter
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.message_actions_presentation.R
import ru.snowadv.message_actions_presentation.api.model.MessageEditorResult
import ru.snowadv.message_actions_presentation.api.model.MessageMoveResult
import ru.snowadv.message_actions_presentation.databinding.FragmentTopicChangerBinding
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerEffectElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerEventElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerStateElm
import ru.snowadv.presentation.adapter.updateIfChanged
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.view.EditTextUtils.observe
import ru.snowadv.presentation.view.setTextIfEmpty
import vivid.money.elmslie.core.store.Store

internal class MessageTopicChangerRenderer :
    ElmFragmentRenderer<MessageTopicChangerBottomSheetDialog, FragmentTopicChangerBinding, MessageTopicChangerEventElm, MessageTopicChangerEffectElm, MessageTopicChangerStateElm> {

    private var _topicsAdapter: ArrayAdapter<String>? = null
    private val topicsAdapter get() = requireNotNull(_topicsAdapter) { "Topics adapter wasn't initialized" }

    override fun MessageTopicChangerBottomSheetDialog.onRendererViewCreated(
        binding: FragmentTopicChangerBinding,
        store: Store<MessageTopicChangerEventElm, MessageTopicChangerEffectElm, MessageTopicChangerStateElm>
    ) {
        initListeners(binding, store)
        _topicsAdapter = ArrayAdapter<String>(binding.root.context, android.R.layout.simple_dropdown_item_1line).also {
            binding.newTopicAutocompleteTextView.setAdapter(it)
        }
    }

    override fun MessageTopicChangerBottomSheetDialog.renderStateByRenderer(
        state: MessageTopicChangerStateElm,
        binding: FragmentTopicChangerBinding
    ) = with(binding) {
        newTopicAutocompleteTextView.setTextIfEmpty(state.currentTopicName)
        stateBox.inflateState(state.topics, R.layout.fragment_topic_changer_shimmer)
        moveMessageButton.isVisible = !state.movingMessage
        movingMessageProgressBar.isVisible = state.movingMessage
        notifyNewTopicCheckbox.isChecked = state.notifyNewTopic
        notifyOldTopicCheckbox.isChecked = state.notifyOldTopic
        topicsAdapter.updateIfChanged(state.topics.data ?: emptyList())
    }

    override fun MessageTopicChangerBottomSheetDialog.handleEffectByRenderer(
        effect: MessageTopicChangerEffectElm,
        binding: FragmentTopicChangerBinding,
        store: Store<MessageTopicChangerEventElm, MessageTopicChangerEffectElm, MessageTopicChangerStateElm>
    ) {
        when (effect) {
            is MessageTopicChangerEffectElm.CloseWithResult -> finishWithResult(effect.result)
            is MessageTopicChangerEffectElm.FinishWithError -> finishWithResult(
                MessageMoveResult.Error(
                    effect.errorMessage ?: getString(R.string.error_has_occurred_please_try_again)
                )
            )
        }
    }

    private fun MessageTopicChangerBottomSheetDialog.initListeners(
        binding: FragmentTopicChangerBinding,
        store: Store<MessageTopicChangerEventElm, MessageTopicChangerEffectElm, MessageTopicChangerStateElm>,
    ) = with(binding) {
        stateBox.setOnRetryClickListener {
            store.accept(MessageTopicChangerEventElm.Ui.OnRetryClicked)
        }
        newTopicAutocompleteTextView.observe().onEach {
            store.accept(MessageTopicChangerEventElm.Ui.ChangedTopic(it))
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .launchIn(viewLifecycleOwner.lifecycleScope)
        moveMessageButton.setOnClickListener {
            store.accept(MessageTopicChangerEventElm.Ui.OnMoveClicked)
        }
        notifyNewTopicCheckbox.setOnCheckedChangeListener { _, isChecked ->
            store.accept(MessageTopicChangerEventElm.Ui.ChangedNotifyNewTopic(isChecked))
        }
        notifyOldTopicCheckbox.setOnCheckedChangeListener { _, isChecked ->
            store.accept(MessageTopicChangerEventElm.Ui.ChangedNotifyOldTopic(isChecked))
        }
    }

    override fun MessageTopicChangerBottomSheetDialog.onDestroyRendererView() {
        _topicsAdapter = null
    }
}
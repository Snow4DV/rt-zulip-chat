package ru.snowadv.channels_presentation.stream_creator

import androidx.core.view.isVisible
import ru.snowadv.channels_presentation.databinding.FragmentStreamCreatorBinding
import ru.snowadv.channels_presentation.stream_creator.elm.StreamCreatorEffectElm
import ru.snowadv.channels_presentation.stream_creator.elm.StreamCreatorEventElm
import ru.snowadv.channels_presentation.stream_creator.elm.StreamCreatorStateElm
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.view.EditTextUtils.addTextChangedNotNullListener
import ru.snowadv.presentation.view.setTextIfEmpty
import vivid.money.elmslie.core.store.Store

internal class StreamCreatorRenderer :
    ElmFragmentRenderer<StreamCreatorBottomSheetDialog, FragmentStreamCreatorBinding, StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm> {

    override fun StreamCreatorBottomSheetDialog.onRendererViewCreated(
        binding: FragmentStreamCreatorBinding,
        store: Store<StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm>
    ) {
        initClickListeners(binding = binding, store = store)
    }

    override fun StreamCreatorBottomSheetDialog.renderStateByRenderer(
        state: StreamCreatorStateElm,
        binding: FragmentStreamCreatorBinding
    ) = with(binding) {
        createStreamButton.isVisible = !state.creatingStream
        creatingStreamProgressBar.isVisible = state.creatingStream

        streamNameEditText.setTextIfEmpty(state.streamName)
        streamDescriptionEditText.setTextIfEmpty(state.description)
        announceCheckbox.isChecked = state.announce
        showHistoryToNewSubsCheckbox.isChecked = state.isHistoryAvailableToNewSubs
    }

    override fun StreamCreatorBottomSheetDialog.handleEffectByRenderer(
        effect: StreamCreatorEffectElm,
        binding: FragmentStreamCreatorBinding,
        store: Store<StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm>
    ) {
        when(effect) {
            is StreamCreatorEffectElm.CloseWithNewStreamCreated -> finishWithNewStreamName(effect.newStreamName)
            StreamCreatorEffectElm.ShowInternetError -> showInternetError(binding.root)
        }
    }

    private fun initClickListeners(
        binding: FragmentStreamCreatorBinding,
        store: Store<StreamCreatorEventElm, StreamCreatorEffectElm, StreamCreatorStateElm>,
    ) = with(binding) {
        streamNameEditText.addTextChangedNotNullListener {
            store.accept(StreamCreatorEventElm.Ui.OnStreamNameChanged(it))
        }
        streamDescriptionEditText.addTextChangedNotNullListener {
            store.accept(StreamCreatorEventElm.Ui.OnStreamDescriptionChanged(it))
        }
        announceCheckbox.setOnCheckedChangeListener { _, isChecked ->
            store.accept(StreamCreatorEventElm.Ui.OnStreamAnnounceChanged(isChecked))
        }
        showHistoryToNewSubsCheckbox.setOnCheckedChangeListener { _, isChecked ->
            store.accept(StreamCreatorEventElm.Ui.OnStreamHistoryAvailableToNewSubsChanged(isChecked))
        }
        createStreamButton.setOnClickListener {
            store.accept(StreamCreatorEventElm.Ui.OnCreateStreamClicked)
        }
    }
}
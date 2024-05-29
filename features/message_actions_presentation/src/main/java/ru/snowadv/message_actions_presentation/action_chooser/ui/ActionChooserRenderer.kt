package ru.snowadv.message_actions_presentation.action_chooser.ui

import android.content.ClipData
import android.content.ClipboardManager
import androidx.core.content.ContextCompat.getSystemService
import ru.snowadv.message_actions_presentation.R
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEffectElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEventElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserStateElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.adapter.MessageActionAdapterDelegate
import ru.snowadv.message_actions_presentation.action_chooser.ui.elm.ActionChooserEffectUiElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.elm.ActionChooserEventUiElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.elm.ActionChooserStateUiElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.model.UiMessageAction
import ru.snowadv.message_actions_presentation.api.model.ActionChooserResult
import ru.snowadv.message_actions_presentation.databinding.FragmentActionChooserBinding
import ru.snowadv.message_actions_presentation.di.dagger.MessageActionsPresentationComponentHolder
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.recycler.setupDecorator
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class ActionChooserRenderer :
    ElmFragmentRenderer<ActionChooserBottomSheetDialog, FragmentActionChooserBinding, ActionChooserEventElm, ActionChooserEffectElm, ActionChooserStateElm> {

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized in ActionChooserRenderer" }

    @Inject
    internal lateinit var mapper: ElmMapper<ActionChooserStateElm, ActionChooserEffectElm, ActionChooserEventElm, ActionChooserStateUiElm, ActionChooserEffectUiElm, ActionChooserEventUiElm>


    override fun ActionChooserBottomSheetDialog.onAttachRendererView() {
        MessageActionsPresentationComponentHolder.getComponent().inject(this@ActionChooserRenderer)
    }

    override fun ActionChooserBottomSheetDialog.onRendererViewCreated(
        binding: FragmentActionChooserBinding,
        store: Store<ActionChooserEventElm, ActionChooserEffectElm, ActionChooserStateElm>
    ) {
        _adapter = setupAdapter(
            binding = binding,
            listener = { action ->
                store.accept(mapper.mapUiEvent(ActionChooserEventUiElm.OnActionChosen(action)))
            },
        )
        binding.actionsRecycler.setupDecorator(horizontalSpacingResId = ru.snowadv.presentation.R.dimen.no_padding)
    }

    override fun ActionChooserBottomSheetDialog.renderStateByRenderer(
        state: ActionChooserStateElm,
        binding: FragmentActionChooserBinding
    ) {
        adapter.submitList(mapper.mapState(state).actions)
    }

    override fun ActionChooserBottomSheetDialog.handleEffectByRenderer(
        effect: ActionChooserEffectElm,
        binding: FragmentActionChooserBinding,
        store: Store<ActionChooserEventElm, ActionChooserEffectElm, ActionChooserStateElm>
    ) {
        when (val mappedEffect = mapper.mapEffect(effect)) {
            is ActionChooserEffectUiElm.CloseWithResult -> finishWithResult(mappedEffect.result)
            is ActionChooserEffectUiElm.CopyMessageToClipboard -> copyToClipboard(mappedEffect.content)
            is ActionChooserEffectUiElm.FinishWithError -> finishWithResult(
                ActionChooserResult.Error(mappedEffect.errorMessage ?: getString(R.string.error_has_occurred_please_try_again))
            )
        }
    }

    override fun ActionChooserBottomSheetDialog.onDestroyRendererView() {
        _adapter = null
    }

    private fun setupAdapter(
        binding: FragmentActionChooserBinding,
        listener: (UiMessageAction) -> Unit
    ): DiffDelegationAdapter {
        return initDelegationAdapter(listener).also { binding.actionsRecycler.adapter = it }
    }

    private fun initDelegationAdapter(listener: (UiMessageAction) -> Unit): DiffDelegationAdapter {
        return DiffDelegationAdapter(initDelegatesManager(listener))
    }

    private fun initDelegatesManager(listener: (UiMessageAction) -> Unit): AdapterDelegatesManager<DelegateItem> {
        return AdapterDelegatesManager(
            MessageActionAdapterDelegate(listener)
        )
    }

    private fun ActionChooserBottomSheetDialog.copyToClipboard(text: String) {
        (getSystemService(requireContext(), ClipboardManager::class.java) as ClipboardManager)
            .setPrimaryClip(
                ClipData.newPlainText(getString(R.string.message_text_clipboard_label), text)
            )
    }
}
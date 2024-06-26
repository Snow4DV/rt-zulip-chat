package ru.snowadv.message_actions_presentation.emoji_chooser.ui

import ru.snowadv.message_actions_presentation.R
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.message_actions_presentation.databinding.FragmentEmojiChooserBinding
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.adapter.EmojiAdapterDelegate
import ru.snowadv.message_actions_presentation.di.dagger.MessageActionsPresentationComponentHolder
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEffectElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserEffectUiElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserEventUiElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserStateUiElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.model.ChatEmoji
import ru.snowadv.model.ScreenState
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class EmojiChooserRenderer :
    ElmFragmentRenderer<EmojiChooserBottomSheetDialog, FragmentEmojiChooserBinding, EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm> {

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized in EmojiChooserDataObserver" }

    @Inject
    internal lateinit var mapper: ElmMapper<EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserEventElm, EmojiChooserStateUiElm, EmojiChooserEffectUiElm, EmojiChooserEventUiElm>


    override fun EmojiChooserBottomSheetDialog.onAttachRendererView() {
        MessageActionsPresentationComponentHolder.getComponent().inject(this@EmojiChooserRenderer)
    }

    override fun EmojiChooserBottomSheetDialog.onRendererViewCreated(
        binding: FragmentEmojiChooserBinding,
        store: Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>
    ) {
        _adapter = setupAdapter(
            binding = binding,
            listener = { emoji ->
                store.accept(mapper.mapUiEvent(EmojiChooserEventUiElm.OnEmojiChosen(emoji)))
            },
        )
        initClickListeners(binding = binding, store = store)
    }

    override fun EmojiChooserBottomSheetDialog.renderStateByRenderer(
        state: EmojiChooserStateElm,
        binding: FragmentEmojiChooserBinding
    ) {
        val mappedState = mapper.mapState(state)

        // Show loading when initializing for the first time
        if (adapter.itemCount == 0 && mappedState.screenState.data?.isNotEmpty() == true) {
            binding.stateBox.inflateState(ScreenState.Loading<Unit>(), R.layout.fragment_emoji_chooser_shimmer)
        }
        adapter.submitList(mappedState.screenState.getCurrentData()) {
            binding.stateBox.inflateState(mappedState.screenState, R.layout.fragment_emoji_chooser_shimmer)
        }
    }

    override fun EmojiChooserBottomSheetDialog.handleEffectByRenderer(
        effect: EmojiChooserEffectElm,
        binding: FragmentEmojiChooserBinding,
        store: Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>
    ) {
        val mappedEffect = mapper.mapEffect(effect)
        when (mappedEffect) {
            is EmojiChooserEffectUiElm.CloseWithChosenEmoji -> finishWithEmoji(mappedEffect.emoji)
        }
    }

    override fun EmojiChooserBottomSheetDialog.onDestroyRendererView() {
        _adapter = null
    }

    private fun setupAdapter(
        binding: FragmentEmojiChooserBinding,
        listener: (ChatEmoji) -> Unit
    ): DiffDelegationAdapter {
        return initDelegationAdapter(listener).also { binding.emojisRecycler.adapter = it }
    }

    private fun initDelegationAdapter(listener: (ChatEmoji) -> Unit): DiffDelegationAdapter {
        return DiffDelegationAdapter(initDelegatesManager(listener))
    }

    private fun initDelegatesManager(listener: (ChatEmoji) -> Unit): AdapterDelegatesManager<DelegateItem> {
        return AdapterDelegatesManager(
            EmojiAdapterDelegate(listener)
        )
    }

    private fun initClickListeners(
        binding: FragmentEmojiChooserBinding,
        store: Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>,
    ) {
        binding.stateBox.setOnRetryClickListener {
            store.accept(EmojiChooserEventElm.Ui.OnRetryClicked)
        }
    }

}
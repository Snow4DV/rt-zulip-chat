package ru.snowadv.chat_impl.presentation.emoji_chooser

import ru.snowadv.chat_impl.R
import ru.snowadv.chat_impl.databinding.FragmentEmojiChooserBinding
import ru.snowadv.chat_impl.presentation.adapter.EmojiAdapterDelegate
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserEffectElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserEventElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserStateElm
import ru.snowadv.chat_impl.presentation.model.ChatEmoji
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import vivid.money.elmslie.core.store.Store

internal class EmojiChooserRenderer :
    ElmFragmentRenderer<EmojiChooserBottomSheetDialog, FragmentEmojiChooserBinding, EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm> {

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized in EmojiChooserDataObserver" }


    override fun EmojiChooserBottomSheetDialog.onRendererViewCreated(
        binding: FragmentEmojiChooserBinding,
        store: Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>
    ) {
        _adapter = setupAdapter(
            binding = binding,
            listener = { emoji ->
                store.accept(EmojiChooserEventElm.Ui.OnEmojiChosen(emoji))
            },
        )
        initClickListeners(binding = binding, store = store)
    }

    override fun EmojiChooserBottomSheetDialog.renderStateByRenderer(
        state: EmojiChooserStateElm,
        binding: FragmentEmojiChooserBinding
    ) {
        adapter.submitList(state.screenState.getCurrentData()) {
            binding.stateBox.inflateState(state.screenState, R.layout.fragment_emoji_chooser_shimmer)
        }
    }

    override fun EmojiChooserBottomSheetDialog.handleEffectByRenderer(
        effect: EmojiChooserEffectElm,
        binding: FragmentEmojiChooserBinding,
        store: Store<EmojiChooserEventElm, EmojiChooserEffectElm, EmojiChooserStateElm>
    ) {
        when (effect) {
            is EmojiChooserEffectElm.CloseWithChosenEmoji -> {
                finishWithEmoji(effect.emoji)
            }
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
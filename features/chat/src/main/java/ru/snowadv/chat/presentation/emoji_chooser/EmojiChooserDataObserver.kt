package ru.snowadv.chat.presentation.emoji_chooser

import android.view.View
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.chat.R
import ru.snowadv.chat.databinding.FragmentEmojiChooserBinding
import ru.snowadv.chat.presentation.adapter.EmojiAdapterDelegate
import ru.snowadv.chat.presentation.emoji_chooser.event.EmojiChooserEvent
import ru.snowadv.chat.presentation.emoji_chooser.event.EmojiChooserFragmentEvent
import ru.snowadv.chat.presentation.emoji_chooser.state.ChooserScreenStatus
import ru.snowadv.chat.presentation.emoji_chooser.state.EmojiChooserState
import ru.snowadv.chat.presentation.emoji_chooser.view_model.EmojiChooserViewModel
import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.util.FragmentDataObserver

internal class EmojiChooserDataObserver :
    FragmentDataObserver<FragmentEmojiChooserBinding, EmojiChooserViewModel, EmojiChooserBottomSheetDialog> {

    override fun EmojiChooserBottomSheetDialog.registerObservingFragment(
        binding: FragmentEmojiChooserBinding,
        viewModel: EmojiChooserViewModel
    ) {
        observeEvents(viewModel, this)
        observeState(binding, viewModel, this)
    }

    private fun observeEvents(
        viewModel: EmojiChooserViewModel,
        fragment: EmojiChooserBottomSheetDialog,
    ) {
        viewModel.fragmentEventFlow
            .onEach { handleEvent(it, fragment) }
            .flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle)
            .launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    private fun handleEvent(
        event: EmojiChooserFragmentEvent,
        fragment: EmojiChooserBottomSheetDialog
    ) {
        when (event) {
            is EmojiChooserFragmentEvent.CloseWithChosenEmoji -> {
                fragment.finishWithEmoji(event.emoji)
            }
        }
    }

    private fun observeState(
        binding: FragmentEmojiChooserBinding,
        viewModel: EmojiChooserViewModel,
        fragment: Fragment
    ) {
        val recyclerAdapter = setupAdapter(
            binding = binding,
            listener = {
                viewModel.event(EmojiChooserEvent.OnEmojiChosen(it))
            }
        )
        viewModel.state
            .onEach { bindState(it, binding, recyclerAdapter) }
            .flowWithLifecycle(fragment.viewLifecycleOwner.lifecycle)
            .launchIn(fragment.viewLifecycleOwner.lifecycleScope)
    }

    private fun bindState(
        state: EmojiChooserState,
        binding: FragmentEmojiChooserBinding,
        recyclerAdapter: DiffDelegationAdapter
    ) {
        when (state.status) {
            ChooserScreenStatus.LOADING -> {
                binding.setVisibleView(binding.loadingBar)
            }

            ChooserScreenStatus.OK -> {
                recyclerAdapter.submitList(state.emojis)
                binding.setVisibleView(binding.emojisRecycler)
            }

            ChooserScreenStatus.ERROR -> {
                binding.setVisibleView(binding.errorIcon)
            }
        }

    }

    private fun FragmentEmojiChooserBinding.setVisibleView(visibleView: View) {
        root.children
            .filter { it.id != R.id.sheet_puller }
            .forEach {
                it.visibility = if (it === visibleView) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
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

}
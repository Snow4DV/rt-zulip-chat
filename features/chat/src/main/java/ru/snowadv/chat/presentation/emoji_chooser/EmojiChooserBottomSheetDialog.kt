package ru.snowadv.chat.presentation.emoji_chooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.snowadv.chat.databinding.FragmentEmojiChooserBinding
import ru.snowadv.chat.presentation.adapter.EmojiAdapterDelegate
import ru.snowadv.chat.presentation.chat.ChatFragment
import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter

typealias OnEmojiClickListener = (ChatEmoji) -> Unit

class EmojiChooserBottomSheetDialog private constructor(
    private val emojis: List<ChatEmoji>,
    private val listener: OnEmojiClickListener? = null
) : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(emojis: List<ChatEmoji>, listener: OnEmojiClickListener? = null) =
            EmojiChooserBottomSheetDialog(emojis, listener)
    }

    private val adapter = initDelegationAdapter().also { it.submitList(emojis) }
    private var _binding: FragmentEmojiChooserBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmojiChooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.emojisRecycler.adapter = adapter
    }

    private fun initDelegationAdapter(): DiffDelegationAdapter {
        return DiffDelegationAdapter(initDelegatesManager())
    }

    private fun initDelegatesManager(): AdapterDelegatesManager<DelegateItem> {
        return AdapterDelegatesManager(
            EmojiAdapterDelegate(
                listener = {
                    this.listener?.invoke(it)
                    this.dismiss()
                }
            )
        )
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
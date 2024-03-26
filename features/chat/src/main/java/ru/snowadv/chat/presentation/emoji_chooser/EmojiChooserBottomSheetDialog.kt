package ru.snowadv.chat.presentation.emoji_chooser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.snowadv.chat.databinding.FragmentEmojiChooserBinding
import ru.snowadv.chat.presentation.emoji_chooser.view_model.EmojiChooserViewModel
import ru.snowadv.chat.presentation.model.ChatEmoji
import ru.snowadv.presentation.util.FragmentDataObserver

typealias OnEmojiClickListener = (ChatEmoji) -> Unit

class EmojiChooserBottomSheetDialog private constructor(
    private val listener: OnEmojiClickListener? = null
) : BottomSheetDialogFragment(),
    FragmentDataObserver<FragmentEmojiChooserBinding, EmojiChooserViewModel, EmojiChooserBottomSheetDialog> by EmojiChooserDataObserver() {

    companion object {
        const val TAG = "emoji_chooser_dialog"
        fun newInstance(listener: OnEmojiClickListener? = null) =
            EmojiChooserBottomSheetDialog(listener)
    }

    private var _binding: FragmentEmojiChooserBinding? = null
    private val viewModel: EmojiChooserViewModel by viewModels()
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
        registerObservingFragment(binding, viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun show(fragmentManager: FragmentManager) = show(fragmentManager, TAG)

    fun finishWithEmoji(emoji: ChatEmoji) {
        this.listener?.invoke(emoji)
        this.dismiss()
    }
}
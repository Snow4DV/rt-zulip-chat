package ru.snowadv.chat.presentation.chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.domain.model.emojiMap
import ru.snowadv.chat.presentation.adapter.OutgoingMessageAdapterDelegate
import ru.snowadv.chat.presentation.chat.view_model.ChatViewModel
import ru.snowadv.chat.presentation.emoji_chooser.EmojiChooserBottomSheetDialog
import ru.snowadv.chat.presentation.util.toUiChatEmoji
import ru.snowadv.presentation.adapter.AdapterDelegate
import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.adapter.impl.AdapterDelegatesManager
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.util.PaddingItemDecorator
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.util.FragmentDataObserver
import ru.snowadv.presentation.util.impl.DayDateFormatter
import ru.snowadv.presentation.util.impl.LocalizedDateTimeFormatter
import java.time.LocalDateTime

class ChatFragment : Fragment(),
    FragmentDataObserver<FragmentChatBinding, ChatViewModel> by ChatFragmentDataObserver() {

    companion object {
        fun newInstance() = ChatFragment()
    }

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentChatBinding.inflate(layoutInflater).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel, this)
        openEmojiChooser()
    }

    private fun openEmojiChooser() {
        val dialog =
            EmojiChooserBottomSheetDialog.newInstance(emojiMap.values.map { it.toUiChatEmoji() })
        dialog.show(childFragmentManager, "test")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package ru.snowadv.chat.presentation.chat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.presentation.chat.view_model.ChatViewModel
import ru.snowadv.presentation.util.FragmentDataObserver

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
        registerObservingFragment(binding, viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
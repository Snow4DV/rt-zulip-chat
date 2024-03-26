package ru.snowadv.chat.presentation.chat

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import ru.snowadv.chat.databinding.FragmentChatBinding
import ru.snowadv.chat.presentation.chat.view_model.ChatViewModel
import ru.snowadv.presentation.R
import ru.snowadv.presentation.adapter.util.PaddingItemDecorator
import ru.snowadv.presentation.util.FragmentDataObserver

class ChatFragment : Fragment(),
    FragmentDataObserver<FragmentChatBinding, ChatViewModel, ChatFragment> by ChatFragmentDataObserver() {

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
        return FragmentChatBinding.inflate(layoutInflater).also {
            _binding = it
            addDecoratorToRecycler(it)
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun addDecoratorToRecycler(binding: FragmentChatBinding) {
        binding.messagesRecycler.addItemDecoration(initDecorator())
    }

    private fun initDecorator(): RecyclerView.ItemDecoration {
        return PaddingItemDecorator(
            requireContext(),
            R.dimen.small_common_padding,
            R.dimen.small_common_padding
        )
    }
}
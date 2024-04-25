package ru.snowadv.chat.presentation.emoji_chooser.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.snowadv.chat.domain.use_case.GetEmojisUseCase

internal class EmojiChooserViewModelFactory(
    private val getEmojisUseCase: GetEmojisUseCase,
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EmojiChooserViewModel::class.java)) {
            return EmojiChooserViewModel(
                getEmojisUseCase = getEmojisUseCase,
            ) as T
        }
        throw IllegalArgumentException("Unknown VM class")
    }
}
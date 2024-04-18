package ru.snowadv.chat.di

import ru.snowadv.chat.domain.use_case.AddReactionUseCase
import ru.snowadv.chat.domain.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat.domain.use_case.GetEmojisUseCase
import ru.snowadv.chat.domain.use_case.ListenToMessagesUseCase
import ru.snowadv.chat.domain.use_case.RemoveReactionUseCase
import ru.snowadv.chat.domain.use_case.SendMessageUseCase

object ChatGraph {
    internal lateinit var deps: ChatDeps
    internal val addReactionUseCase by lazy { AddReactionUseCase(deps.messageRepository) }
    internal val removeReactionUseCase by lazy { RemoveReactionUseCase(deps.messageRepository) }
    internal val getCurrentMessagesUseCase by lazy { GetCurrentMessagesUseCase(deps.messageRepository) }
    internal val listenToMessagesUseCase by lazy { ListenToMessagesUseCase(deps.messageRepository) }
    internal val sendMessageUseCase by lazy { SendMessageUseCase(deps.messageRepository) }
    internal val getEmojisUseCase by lazy { GetEmojisUseCase(deps.emojiRepository) }

    fun init(deps: ChatDeps) {
        this.deps = deps
    }
}
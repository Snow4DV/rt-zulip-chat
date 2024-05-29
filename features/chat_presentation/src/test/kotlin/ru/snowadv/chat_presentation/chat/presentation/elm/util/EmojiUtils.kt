package ru.snowadv.chat_presentation.chat.presentation.elm.util

import ru.snowadv.chat_presentation.chat.ui.model.ChatReaction as UiChatReaction
import ru.snowadv.chat_domain_api.model.ChatReaction

internal object EmojiUtils {
    fun generateReactions(count: Int, maxReactionsCount: Int, reactionNamesToCode: List<Pair<String, String>>): List<ChatReaction> {
        return List(count % maxReactionsCount) {
            ChatReaction(reactionNamesToCode[it].first, reactionNamesToCode[it].second, it + 2, false)
        }
    }

    fun generateUiReactions(count: Int, maxReactionsCount: Int, reactionNamesToCode: List<Pair<String, String>>): List<UiChatReaction> {
        return List(count % maxReactionsCount) {
            UiChatReaction(reactionNamesToCode[it].first, reactionNamesToCode[it].second, it + 2, false, "$it")
        }
    }

    //ChatReaction(name=happy, emojiCode=1f600, count=2, userReacted=false, emojiString=😀)
    fun generateSingleTypeReactions(count: Int) : List<ChatReaction> {
        return List(count) {
            ChatReaction(name = "happy", emojiCode = "1f600", count=2, userReacted = count % 2 == 0)
        }
    }

    fun generateSingleTypeUiReactions(count: Int) : List<UiChatReaction> {
        return List(count) {
            UiChatReaction(name = "happy", emojiCode = "1f600", count=2, userReacted = count % 2 == 0, emojiString = "\uD83D\uDE00")
        }
    }
}
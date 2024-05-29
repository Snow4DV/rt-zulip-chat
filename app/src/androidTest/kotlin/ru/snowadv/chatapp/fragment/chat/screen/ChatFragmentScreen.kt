package ru.snowadv.chatapp.fragment.chat.screen

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.progress.KProgressBar
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import ru.snowadv.chat_presentation.R

import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.chatapp.fragment.chat.custom_view.KReactionView
import ru.snowadv.chatapp.fragment.chat.custom_view.WithReactionCountMatcher

internal object ChatFragmentScreen : KScreen<ChatFragmentScreen>() {
    override val layoutId: Int = R.layout.fragment_chat
    override val viewClass: Class<*> = ChatFragment::class.java

    val streamTitle = KTextView { withId(ru.snowadv.presentation.R.id.bar_title) }
    val backButton = KButton { withId(ru.snowadv.presentation.R.id.back_button) }

    val topicTitle = KTextView { withId(R.id.topic_name) }

    val progressBar = KProgressBar { withId(R.id.action_progress_bar) }

    val messagesRecycler = KRecyclerView(
        builder = { withId(R.id.messages_recycler) },
        itemTypeBuilder = {
            itemType(ChatFragmentScreen::KOutgoingMessageItem)
            itemType(ChatFragmentScreen::KIncomingMessageItem)
            itemType(ChatFragmentScreen::KDateSplitterItem)
            itemType(ChatFragmentScreen::KPaginationAllDoneItem)
            itemType(ChatFragmentScreen::KPaginationErrorItem)
            itemType(ChatFragmentScreen::KPaginationHasMoreItem)
        }
    )


    class KOutgoingMessageItem(parent: Matcher<View>): KRecyclerItem<KOutgoingMessageItem>(parent) {
        val content = KTextView(parent) { withId(R.id.message_text) }
        val timestampText = KTextView(parent) { withId(R.id.message_timestamp) }
        val reactionsContainer = KView(parent) { withId(R.id.reactions_container) }

        val oneVoteReaction = KReactionView(parent) {
            withParent { withId(R.id.reactions_container)  }
            withMatcher(WithReactionCountMatcher(`is`(1)))
        }
    }

    class KIncomingMessageItem(parent: Matcher<View>): KRecyclerItem<KIncomingMessageItem>(parent) {
        val usernameAvatar = KImageView(parent) { withId(R.id.avatar) }
        val usernameText = KTextView(parent) { withId(R.id.message_user_name) }
        val content = KTextView(parent) { withId(R.id.message_text) }
        val timestampText = KTextView(parent) { withId(R.id.message_timestamp) }
        val reactionsContainer = KView(parent) { withId(R.id.reactions_container) }

        val oneVoteReaction = KReactionView(parent) {
            withParent { withId(R.id.reactions_container)  }
            withMatcher(WithReactionCountMatcher(`is`(1)))
        }
        val twoVotesReaction = KReactionView(parent) {
            withParent { withId(R.id.reactions_container)  }
            withMatcher(WithReactionCountMatcher(`is`(2)))
        }
        val selectedReaction = KReactionView(parent) {
            withParent { withId(R.id.reactions_container)  }
            withMatcher(ViewMatchers.isSelected())
        }
    }

    class KDateSplitterItem(parent: Matcher<View>): KRecyclerItem<KDateSplitterItem>(parent) {
        val dateText = KTextView(parent) { withId(R.id.date_text) }
    }

    class KPaginationAllDoneItem(parent: Matcher<View>): KRecyclerItem<KPaginationAllDoneItem>(parent) {
        val allDoneText = KTextView(parent) { withId(R.id.all_done_text) }
    }

    class KPaginationErrorItem(parent: Matcher<View>): KRecyclerItem<KPaginationErrorItem>(parent) {
        val errorIcon = KImageView(parent) { withId(R.id.error_icon) }
        val errorText = KTextView(parent) { withId(R.id.error_text) }
    }

    class KPaginationHasMoreItem(parent: Matcher<View>): KRecyclerItem<KPaginationHasMoreItem>(parent) {
        val moreIcon = KImageView(parent) { withId(R.id.more_icon) }
        val moreText = KTextView(parent) { withId(R.id.more_text) }
    }


    // State box children
    val emptyBox = KView { withId(ru.snowadv.presentation.R.id.empty_box) }
    val errorBox = KView { withId(ru.snowadv.presentation.R.id.error_box) }
    val errorBoxWithCachedData = KView { withId(ru.snowadv.presentation.R.id.error_with_cached_data) }
    val loadingBox = KView { withId(ru.snowadv.presentation.R.id.loading_box) }
    val loadingBoxWithCachedData = KView { withId(ru.snowadv.presentation.R.id.loading_with_cached_data) }

    val states = listOf(emptyBox, errorBox , errorBoxWithCachedData, loadingBox, loadingBoxWithCachedData)

    val messageEditText = KEditText { withId(R.id.message_edit_text) }
    val sendOrAddAttachmentButton = KImageView { withId(R.id.send_or_add_attachment_button) }

    fun isVisible() {

    }
}
package ru.snowadv.chatapp.fragment.channels.screen

import android.view.View
import androidx.test.espresso.matcher.ViewMatchers
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.pager2.KViewPager2
import io.github.kakaocup.kakao.pager2.KViewPagerItem
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import org.junit.Assert
import ru.snowadv.channels_presentation.channel_list.ChannelListFragment
import ru.snowadv.chatapp.R

internal object ChannelsFragmentScreen : KScreen<ChannelsFragmentScreen>() {
    override val layoutId: Int = ru.snowadv.channels_presentation.R.layout.fragment_channel_list
    override val viewClass: Class<*> = ChannelListFragment::class.java

    val searchEditText = KEditText {
        withId(ru.snowadv.channels_presentation.R.id.search_edit_text)
        withParent { withId(ru.snowadv.channels_presentation.R.id.channels_search_bar) }
    }
    val searchIcon = KImageView {
        withId(ru.snowadv.channels_presentation.R.id.search_icon)
        withParent { withId(ru.snowadv.channels_presentation.R.id.channels_search_bar) }
    }

    val channelsPager = KViewPager2(
        builder = { withId(ru.snowadv.channels_presentation.R.id.stream_types_pager) },
        itemTypeBuilder = {
            itemType(::KStreamsRecyclerItem)
        }
    )



    class KStreamsRecyclerItem(parent: Matcher<View>): KViewPagerItem<KStreamsRecyclerItem>(parent) {
        val streamsRecycler = KRecyclerView(
            builder = { withId(ru.snowadv.channels_presentation.R.id.streams_recycler) },
            itemTypeBuilder = {
                itemType(::KStreamItem)
                itemType(::KTopicItem)
            }
        )
    }


    class KStreamItem(parent: Matcher<View>): KRecyclerItem<KStreamItem>(parent) {
        val stream = KView(parent) { withId(ru.snowadv.channels_presentation.R.id.stream_item) }
        val streamNameText = KTextView(parent) { withId(ru.snowadv.channels_presentation.R.id.stream_name_text) }
        val expandButton = KImageView(parent) { withId(ru.snowadv.channels_presentation.R.id.expand_stream_button) }
    }

    class KTopicItem(parent: Matcher<View>): KRecyclerItem<KTopicItem>(parent) {
        val topic = KView(parent) { withId(ru.snowadv.channels_presentation.R.id.topic_item) }
        val topicNameText = KTextView(parent) { withId(ru.snowadv.channels_presentation.R.id.topic_name_text) }
    }

    fun isVisible() {
        channelsPager.isVisible()
        Assert.assertTrue(channelsPager.getSize() > 0)
        channelsPager.childAt<KStreamsRecyclerItem>(0) {
            streamsRecycler.isVisible()
        }
        searchEditText.isVisible()
        searchIcon.isVisible()
    }
}
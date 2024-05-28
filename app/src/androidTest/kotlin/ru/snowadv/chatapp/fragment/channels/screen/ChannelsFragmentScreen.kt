package ru.snowadv.chatapp.fragment.channels.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.pager2.KViewPager2
import io.github.kakaocup.kakao.pager2.KViewPagerItem
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.snowadv.channels_presentation.channel_list.ChannelListFragment

internal object ChannelsFragmentScreen : KScreen<ChannelsFragmentScreen>() {
    override val layoutId: Int = ru.snowadv.channels_presentation.R.layout.fragment_channel_list
    override val viewClass: Class<*> = ChannelListFragment::class.java

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
}
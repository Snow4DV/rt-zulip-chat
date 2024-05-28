package ru.snowadv.chatapp.fragment.people.screen

import android.view.View
import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.recycler.KRecyclerItem
import io.github.kakaocup.kakao.recycler.KRecyclerView
import io.github.kakaocup.kakao.text.KTextView
import org.hamcrest.Matcher
import ru.snowadv.people_presentation.ui.PeopleFragment

internal object PeopleFragmentScreen : KScreen<PeopleFragmentScreen>() {
    override val layoutId: Int = ru.snowadv.people_presentation.R.layout.fragment_people
    override val viewClass: Class<*> = PeopleFragment::class.java

    val searchEditText = KEditText { withId(ru.snowadv.people_presentation.R.id.search_edit_text) }
    val searchIcon = KImageView { withId(ru.snowadv.people_presentation.R.id.search_icon) }


    val peopleRecycler = KRecyclerView(
        builder = { withId(ru.snowadv.people_presentation.R.id.people_recycler) },
        itemTypeBuilder = {
            itemType(::KPeopleItem)
        }
    )


    class KPeopleItem(parent: Matcher<View>): KRecyclerItem<KPeopleItem>(parent) {
        val userAvatar = KImageView(parent) { withId(ru.snowadv.people_presentation.R.id.user_avatar) }
        val userStatus = KImageView(parent) { withId(ru.snowadv.people_presentation.R.id.user_status) }
        val userName = KTextView(parent) { withId(ru.snowadv.people_presentation.R.id.user_name) }
        val userEmail = KTextView(parent) { withId(ru.snowadv.people_presentation.R.id.user_email) }
    }


}
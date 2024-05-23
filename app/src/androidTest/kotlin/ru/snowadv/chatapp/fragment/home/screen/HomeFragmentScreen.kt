package ru.snowadv.chatapp.fragment.home.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.bottomnav.KBottomNavigationView
import io.github.kakaocup.kakao.common.views.KView
import ru.snowadv.home_presentation.R
import ru.snowadv.home_presentation.home.HomeFragment

internal object HomeFragmentScreen : KScreen<HomeFragmentScreen>() {
    override val layoutId: Int = R.layout.fragment_home
    override val viewClass: Class<*> = HomeFragment::class.java

    val homeScreensIds by lazy {
        listOf(
            ru.snowadv.channels_presentation.R.id.fragment_channel_list,
            ru.snowadv.people_presentation.R.id.fragment_people,
            ru.snowadv.profile_presentation.R.id.fragment_profile,
        )
    }

    val bottomNavigationView = KBottomNavigationView { withId(R.id.bottom_navigation) }

    val fragmentContainer = KView { withId(R.id.home_fragment_container) }
}
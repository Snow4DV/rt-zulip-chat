package ru.snowadv.chatapp.fragment.profile.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.snowadv.profile_presentation.ui.ProfileFragment

internal object ProfileFragmentScreen : KScreen<ProfileFragmentScreen>() {
    override val layoutId: Int = ru.snowadv.profile_presentation.R.layout.fragment_profile
    override val viewClass: Class<*> = ProfileFragment::class.java

    val userName = KTextView { withId(ru.snowadv.profile_presentation.R.id.profile_user_name) }
    val userEmail = KTextView { withId(ru.snowadv.profile_presentation.R.id.profile_user_email) }
    val userAvatar = KImageView { withId(ru.snowadv.profile_presentation.R.id.profile_user_avatar) }
    val userStatus = KTextView { withId(ru.snowadv.profile_presentation.R.id.profile_user_status) }
    val logoutButton = KButton { withId(ru.snowadv.profile_presentation.R.id.button_logout) }
    fun isVisible() {
        userName.isVisible()
        userEmail.isVisible()
        userAvatar.isVisible()
        userStatus.isVisible()
    }
}
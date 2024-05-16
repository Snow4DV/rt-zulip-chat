package ru.snowadv.chatapp.fragment.profile.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.image.KImageView
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.snowadv.chat_presentation.R

import ru.snowadv.chat_presentation.chat.ui.ChatFragment
import ru.snowadv.profile_impl.presentation.profile.ProfileFragment

internal object ProfileFragmentScreen : KScreen<ProfileFragmentScreen>() {
    override val layoutId: Int = ru.snowadv.profile_impl.R.layout.fragment_profile
    override val viewClass: Class<*> = ProfileFragment::class.java

    val userName = KTextView { withId(ru.snowadv.profile_impl.R.id.user_name) }
    val userEmail = KTextView { withId(ru.snowadv.profile_impl.R.id.user_email) }
    val userAvatar = KImageView { withId(ru.snowadv.profile_impl.R.id.user_avatar) }
    val userStatus = KTextView { withId(ru.snowadv.profile_impl.R.id.user_status) }
    val logoutButton = KButton { withId(ru.snowadv.profile_impl.R.id.button_logout) }
}
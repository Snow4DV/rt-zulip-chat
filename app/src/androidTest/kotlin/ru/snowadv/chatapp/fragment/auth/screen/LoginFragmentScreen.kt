package ru.snowadv.chatapp.fragment.auth.screen

import com.kaspersky.kaspresso.screens.KScreen
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.text.KButton
import io.github.kakaocup.kakao.text.KTextView
import ru.snowadv.auth_presentation.login.LoginFragment
import ru.snowadv.auth_presentation.R

object LoginFragmentScreen : KScreen<LoginFragmentScreen>() {
    override val layoutId: Int = R.layout.fragment_login
    override val viewClass: Class<*> = LoginFragment::class.java

    val progressBar = KView { withId(R.id.action_progress_bar) }

    val loginTitle = KTextView { withId(R.id.login_title) }

    val emailInput = KEditText {
        withId(R.id.email)
        withParent { withId(R.id.email_box) }
    }

    val passwordInput = KEditText {
        withId(R.id.password)
        withParent { withId(R.id.password_box) }
    }

    val loginButton = KButton { withId(R.id.button_login) }
}

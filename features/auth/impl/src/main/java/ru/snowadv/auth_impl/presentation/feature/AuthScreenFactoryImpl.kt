package ru.snowadv.auth_impl.presentation.feature

import androidx.fragment.app.Fragment
import dagger.Reusable
import ru.snowadv.auth_api.presentation.AuthScreenFactory
import ru.snowadv.auth_impl.presentation.login.LoginFragment
import javax.inject.Inject

@Reusable
internal class AuthScreenFactoryImpl @Inject constructor() : AuthScreenFactory {
    override fun create(): Fragment {
        return LoginFragment.newInstance()
    }

}
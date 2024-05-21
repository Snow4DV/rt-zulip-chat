package ru.snowadv.auth_impl.presentation.login.elm

import ru.snowadv.presentation.adapter.DelegateItem
import ru.snowadv.presentation.model.ScreenState

internal data class LoginStateElm(
    val loading: Boolean,
    val email: String,
    val password: String,
)
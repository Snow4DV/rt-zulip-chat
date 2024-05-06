package ru.snowadv.auth_api.presentation

import androidx.fragment.app.Fragment

interface AuthScreenFactory {
    fun create(): Fragment
}
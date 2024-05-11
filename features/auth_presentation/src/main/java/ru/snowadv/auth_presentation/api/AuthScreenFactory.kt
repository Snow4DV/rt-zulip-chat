package ru.snowadv.auth_presentation.api

import androidx.fragment.app.Fragment

interface AuthScreenFactory {
    fun create(): Fragment
}
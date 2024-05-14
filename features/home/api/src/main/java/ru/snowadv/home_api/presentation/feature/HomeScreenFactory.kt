package ru.snowadv.home_api.presentation.feature

import androidx.fragment.app.Fragment

interface HomeScreenFactory {
    fun create(): Fragment
}
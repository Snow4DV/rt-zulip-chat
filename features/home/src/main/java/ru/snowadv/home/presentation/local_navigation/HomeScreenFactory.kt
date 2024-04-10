package ru.snowadv.home.presentation.local_navigation

import androidx.fragment.app.Fragment

interface HomeScreenFactory {
    fun createFragment(screen: HomeScreen): Fragment
}
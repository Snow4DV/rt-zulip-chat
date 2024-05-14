package ru.snowadv.home_api.presentation.local_navigation

import androidx.fragment.app.Fragment

interface InnerHomeScreenFactory {
    fun createFragment(screen: InnerHomeScreen): Fragment
}
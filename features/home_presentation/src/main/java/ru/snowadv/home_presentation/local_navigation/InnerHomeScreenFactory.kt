package ru.snowadv.home_presentation.local_navigation

import androidx.fragment.app.Fragment
import ru.snowadv.home_presentation.model.InnerHomeScreen

interface InnerHomeScreenFactory {
    fun createFragment(screen: InnerHomeScreen): Fragment
}
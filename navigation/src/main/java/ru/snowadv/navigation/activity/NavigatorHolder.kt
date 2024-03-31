package ru.snowadv.navigation.activity

import android.app.Activity
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import ru.snowadv.navigation.application.NavigationHolder

interface NavigatorHolder {
    val navigator: AppNavigator

    fun Activity.getNavigatorHolder(): NavigatorHolder {
        return (application as? NavigationHolder)?.navigationHolder ?: error("Activity is not attached to navigation holder")
    }
}
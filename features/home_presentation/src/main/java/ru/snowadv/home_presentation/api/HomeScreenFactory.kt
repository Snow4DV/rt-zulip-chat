package ru.snowadv.home_presentation.api

import androidx.fragment.app.Fragment

interface HomeScreenFactory {
    fun create(): Fragment
}
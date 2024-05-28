package ru.snowadv.people_presentation.api

import androidx.fragment.app.Fragment

interface PeopleScreenFactory {
    fun create(): Fragment
}
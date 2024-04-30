package ru.snowadv.people_api.presentation

import androidx.fragment.app.Fragment

interface PeopleScreenFactory {
    fun create(): Fragment
}
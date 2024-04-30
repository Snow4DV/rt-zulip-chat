package ru.snowadv.channels_api.presentation

import androidx.fragment.app.Fragment

interface ChannelsScreenFactory {
    fun create(): Fragment
}
package ru.snowadv.app_contacts.presentation.common

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

internal interface ObserverActivity<B: ViewBinding, VM: ViewModel> {
    fun registerObservingActivity(binding: B, viewModel: VM, activity: ComponentActivity)
}
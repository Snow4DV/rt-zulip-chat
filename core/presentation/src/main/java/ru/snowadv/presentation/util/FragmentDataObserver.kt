package ru.snowadv.presentation.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

interface FragmentDataObserver<B: ViewBinding, VM: ViewModel> {
    fun Fragment.registerObservingFragment(binding: B, viewModel: VM)
}
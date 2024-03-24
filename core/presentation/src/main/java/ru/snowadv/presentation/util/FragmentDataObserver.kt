package ru.snowadv.presentation.util

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

interface FragmentDataObserver<B: ViewBinding, VM: ViewModel> {
    fun registerObservingFragment(binding: B, viewModel: VM, fragment: Fragment)
}
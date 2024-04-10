package ru.snowadv.presentation.fragment

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding

interface FragmentDataObserver<B: ViewBinding, VM: ViewModel, F: Fragment> {
    fun F.registerObservingFragment(binding: B, viewModel: VM)

    fun F.onViewDestroyedToObserver() {

    }
}
package ru.snowadv.home.presentation.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import ru.snowadv.home.R
import ru.snowadv.home.databinding.FragmentHomeBinding
import ru.snowadv.home.di.HomeGraph
import ru.snowadv.home.presentation.home.view_model.HomeViewModel
import ru.snowadv.home.presentation.local_navigation.HomeScreen
import ru.snowadv.presentation.activity.hideKeyboard
import ru.snowadv.presentation.fragment.FragmentDataObserver

class HomeFragment : Fragment(),
    FragmentDataObserver<FragmentHomeBinding, HomeViewModel, HomeFragment> by HomeFragmentDataObserver() {

    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden }

    companion object {
        fun newInstance(): Fragment = HomeFragment()
    }

    private val viewModel: HomeViewModel by viewModels()
    private val factory by lazy { HomeGraph.deps.homeScreenFactory }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding) {"Binding wasn't initialized"}

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentHomeBinding.inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel)
    }

    fun selectTab(screen: HomeScreen) {
        activity?.hideKeyboard()
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(screen.tag)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return
        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(
                R.id.fragment_container,
                factory.createFragment(screen),
                screen.tag
            )

            currentFragment?.let { fragment ->
                hide(fragment)
                setMaxLifecycle(fragment, Lifecycle.State.STARTED)
            }

            newFragment?.let { fragment ->
                show(fragment)
                setMaxLifecycle(fragment, Lifecycle.State.RESUMED)
            }
        }.commitNow()
    }
}
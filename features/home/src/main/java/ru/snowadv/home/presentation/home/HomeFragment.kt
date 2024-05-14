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
import ru.snowadv.home.presentation.home.elm.HomeEffectElm
import ru.snowadv.home.presentation.home.elm.HomeEventElm
import ru.snowadv.home.presentation.home.elm.HomeStateElm
import ru.snowadv.home.presentation.home.elm.HomeStoreFactoryElm
import ru.snowadv.home.presentation.local_navigation.HomeScreen
import ru.snowadv.presentation.activity.hideKeyboard
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.FragmentDataObserver
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store

internal class HomeFragment : BaseFragment<HomeEventElm, HomeEffectElm, HomeStateElm>(),
    ElmFragmentRenderer<HomeFragment, FragmentHomeBinding, HomeEventElm, HomeEffectElm, HomeStateElm> by HomeFragmentRenderer() {

    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden }

    companion object {
        fun newInstance(): Fragment = HomeFragment()
    }

    private val factory by lazy { HomeGraph.deps.homeScreenFactory }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding) {"Binding wasn't initialized"}
    override val store: Store<HomeEventElm, HomeEffectElm, HomeStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        HomeStoreFactoryElm(HomeGraph.homeActorElm).create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentHomeBinding.inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
    }

    override fun render(state: HomeStateElm) {
        renderStateByRenderer(state, binding)
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
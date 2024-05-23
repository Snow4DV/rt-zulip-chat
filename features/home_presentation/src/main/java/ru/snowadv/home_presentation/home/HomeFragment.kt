package ru.snowadv.home_presentation.home

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import ru.snowadv.home_presentation.R
import ru.snowadv.home_presentation.local_navigation.InnerHomeScreenFactory
import ru.snowadv.home_presentation.home.elm.HomeEffectElm
import ru.snowadv.home_presentation.home.elm.HomeEventElm
import ru.snowadv.home_presentation.home.elm.HomeStateElm
import ru.snowadv.home_presentation.home.elm.HomeStoreFactoryElm
import ru.snowadv.home_presentation.databinding.FragmentHomeBinding
import ru.snowadv.home_presentation.di.dagger.HomePresentationComponentHolder
import ru.snowadv.home_presentation.model.InnerHomeScreen
import ru.snowadv.presentation.activity.hideKeyboard
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class HomeFragment : BaseFragment<HomeEventElm, HomeEffectElm, HomeStateElm>(),
    ElmFragmentRenderer<HomeFragment, FragmentHomeBinding, HomeEventElm, HomeEffectElm, HomeStateElm> by HomeFragmentRenderer() {

    private val currentTabFragment: Fragment?
        get() = childFragmentManager.fragments.firstOrNull { !it.isHidden }

    companion object {
        fun newInstance(): Fragment = HomeFragment()
    }

    @Inject
    internal lateinit var factory: InnerHomeScreenFactory
    @Inject
    internal lateinit var homeStoreFactoryElm: HomeStoreFactoryElm

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = requireNotNull(_binding) {"Binding wasn't initialized"}
    override val store: Store<HomeEventElm, HomeEffectElm, HomeStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        homeStoreFactoryElm.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentHomeBinding.inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        HomePresentationComponentHolder.getComponent().inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
    }

    override fun render(state: HomeStateElm) {
        renderStateByRenderer(state, binding)
    }

    fun selectTab(screen: InnerHomeScreen) {
        activity?.hideKeyboard()
        val currentFragment = currentTabFragment
        val newFragment = childFragmentManager.findFragmentByTag(screen.tag)

        if (currentFragment != null && newFragment != null && currentFragment == newFragment) return
        childFragmentManager.beginTransaction().apply {
            if (newFragment == null) add(
                R.id.home_fragment_container,
                factory.createFragment(screen),
                screen.tag,
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
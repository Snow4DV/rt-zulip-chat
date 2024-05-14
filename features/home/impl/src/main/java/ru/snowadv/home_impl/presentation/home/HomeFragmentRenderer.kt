package ru.snowadv.home_impl.presentation.home

import ru.snowadv.home_impl.presentation.home.elm.HomeEffectElm
import ru.snowadv.home_impl.presentation.home.elm.HomeEventElm
import ru.snowadv.home_impl.presentation.home.elm.HomeStateElm
import ru.snowadv.home_impl.databinding.FragmentHomeBinding
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.core.store.Store

internal class HomeFragmentRenderer :
    ElmFragmentRenderer<HomeFragment, FragmentHomeBinding, HomeEventElm, HomeEffectElm, HomeStateElm> {


    override fun HomeFragment.onRendererViewCreated(
        binding: FragmentHomeBinding,
        store: Store<HomeEventElm, HomeEffectElm, HomeStateElm>
    ) {
        initListeners(store, binding)
    }

    override fun HomeFragment.renderStateByRenderer(
        state: HomeStateElm,
        binding: FragmentHomeBinding
    ) {
        if (state.currentScreen.menuActionId != binding.bottomNavigation.selectedItemId) {
            binding.bottomNavigation.selectedItemId = state.currentScreen.menuActionId
        }
        selectTab(state.currentScreen)
    }

    private fun initListeners(
        store: Store<HomeEventElm, HomeEffectElm, HomeStateElm>,
        binding: FragmentHomeBinding,
    ) {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            ru.snowadv.home_api.presentation.local_navigation.InnerHomeScreen.getByActionId(menuItem.itemId).let { homeScreen ->
                store.accept(HomeEventElm.Ui.OnBottomTabSelected(homeScreen))
            }
            true
        }
    }
}
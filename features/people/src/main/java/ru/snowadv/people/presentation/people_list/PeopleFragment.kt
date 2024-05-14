package ru.snowadv.people.presentation.people_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.snowadv.people.databinding.FragmentPeopleBinding
import ru.snowadv.people.di.PeopleGraph
import ru.snowadv.people.presentation.people_list.elm.PeopleListEffectElm
import ru.snowadv.people.presentation.people_list.elm.PeopleListEventElm
import ru.snowadv.people.presentation.people_list.elm.PeopleListStateElm
import ru.snowadv.people.presentation.people_list.elm.PeopleListStoreFactoryElm
import ru.snowadv.presentation.activity.showKeyboard
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store

internal class PeopleFragment : BaseFragment<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>(),
    ElmFragmentRenderer<PeopleFragment, FragmentPeopleBinding, PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>
    by PeopleListRenderer() {

    companion object {
        fun newInstance(): Fragment = PeopleFragment()
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }
    override val store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        PeopleListStoreFactoryElm(PeopleGraph.peopleListActorElm).create()
    }

    override val resumeUiEvent: PeopleListEventElm = PeopleListEventElm.Ui.Resumed
    override val pauseUiEvent: PeopleListEventElm = PeopleListEventElm.Ui.Paused

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentPeopleBinding.inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
    }

    override fun render(state: PeopleListStateElm) {
        renderStateByRenderer(state, binding)
    }


    override fun handleEffect(effect: PeopleListEffectElm) {
        handleEffectByRenderer(effect, binding, store)
    }

    fun focusOnSearchFieldAndOpenKeyboard() {
        binding.searchBar.searchEditText.requestFocus()
        activity?.showKeyboard(binding.searchBar.searchEditText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyRendererView()
    }
}
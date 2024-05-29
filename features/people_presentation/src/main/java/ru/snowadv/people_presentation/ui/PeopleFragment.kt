package ru.snowadv.people_presentation.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.snowadv.people_presentation.databinding.FragmentPeopleBinding
import ru.snowadv.people_presentation.di.holder.PeoplePresentationComponentHolder
import ru.snowadv.people_presentation.presentation.elm.PeopleListEffectElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListEventElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListStateElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListStoreFactoryElm
import ru.snowadv.presentation.activity.showKeyboard
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class PeopleFragment : BaseFragment<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>(),
    ElmFragmentRenderer<PeopleFragment, FragmentPeopleBinding, PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>
    by PeopleRenderer() {

    companion object {
        fun newInstance(): Fragment = PeopleFragment()
    }

    @Inject
    internal lateinit var peopleListStoreFactoryElm: PeopleListStoreFactoryElm

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = requireNotNull(_binding) { "Binding wasn't initialized" }
    override val store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        peopleListStoreFactoryElm.create()
    }

    override val resumeUiEvent: PeopleListEventElm = PeopleListEventElm.Ui.Resumed
    override val pauseUiEvent: PeopleListEventElm = PeopleListEventElm.Ui.Paused

    override fun onAttach(context: Context) {
        super.onAttach(context)
        PeoplePresentationComponentHolder.getComponent().inject(this)
        onAttachRendererView()
    }

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
        binding.peopleSearchBar.searchEditText.requestFocus()
        activity?.showKeyboard(binding.peopleSearchBar.searchEditText)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyRendererView()
    }
}
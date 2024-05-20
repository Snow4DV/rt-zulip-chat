package ru.snowadv.people_presentation.ui

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.people_presentation.ui.adapter.PeopleAdapterDelegate
import ru.snowadv.people_presentation.R
import ru.snowadv.people_presentation.databinding.FragmentPeopleBinding
import ru.snowadv.people_presentation.di.holder.PeoplePresentationComponentHolder
import ru.snowadv.people_presentation.presentation.elm.PeopleListEffectElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListEventElm
import ru.snowadv.people_presentation.presentation.elm.PeopleListStateElm
import ru.snowadv.people_presentation.ui.elm.PeopleListEffectUiElm
import ru.snowadv.people_presentation.ui.elm.PeopleListEventUiElm
import ru.snowadv.people_presentation.ui.elm.PeopleListStateUiElm
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.setupDiffDelegatesAdapter
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.recycler.setupDecorator
import ru.snowadv.presentation.view.setTextIfChanged
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class PeopleRenderer :
    ElmFragmentRenderer<PeopleFragment, FragmentPeopleBinding, PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm> {

    companion object {
        const val SEARCH_DEBOUNCE_RATE_MS = 200L
    }

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized in PeopleListFragment" }
    private val searchQueryPublisher =
        MutableSharedFlow<String>(extraBufferCapacity = 1) // Used to debounce search

    @Inject
    internal lateinit var mapper: ElmMapper<PeopleListStateElm, PeopleListEffectElm, PeopleListEventElm, PeopleListStateUiElm, PeopleListEffectUiElm, PeopleListEventUiElm>

    override fun PeopleFragment.onAttachRendererView() {
        PeoplePresentationComponentHolder.getComponent().inject(this@PeopleRenderer)
    }

    override fun PeopleFragment.onRendererViewCreated(
        binding: FragmentPeopleBinding,
        store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>,
    ) {
        _adapter = setupDelegateAdapter(store)
        binding.peopleRecycler.setupDecorator(
            horizontalSpacingResId = ru.snowadv.presentation.R.dimen.common_padding,
            verticalSpacingResId = R.dimen.people_vertical_padding,
        )
        binding.peopleRecycler.adapter = adapter
        initListeners(binding, store)
    }

    override fun PeopleFragment.renderStateByRenderer(
        state: PeopleListStateElm,
        binding: FragmentPeopleBinding,
    ) = with(binding) {
        val mappedState = mapper.mapState(state)
        stateBox.inflateState(mappedState.screenState, R.layout.fragment_people_shimmer, topStateBox)
        adapter.submitList(mappedState.screenState.getCurrentData())
        searchBar.searchEditText.setTextIfChanged(mappedState.searchQuery)
    }

    override fun PeopleFragment.handleEffectByRenderer(
        effect: PeopleListEffectElm,
        binding: FragmentPeopleBinding,
        store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>
    ) {
        val mappedEffect = mapper.mapEffect(effect)
        when(mappedEffect) {
            PeopleListEffectUiElm.FocusOnSearchFieldAndOpenKeyboard -> focusOnSearchFieldAndOpenKeyboard()
        }
    }

    override fun PeopleFragment.onDestroyRendererView() {
        _adapter = null
    }

    private fun PeopleFragment.initListeners(
        binding: FragmentPeopleBinding,
        store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>,
    ) {
        binding.searchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let { text ->
                searchQueryPublisher.tryEmit(text)
            }
        }
        binding.stateBox.setOnRetryClickListener {
            store.accept(mapper.mapUiEvent(PeopleListEventUiElm.ClickedOnRetry))
        }
        binding.searchBar.searchIcon.setOnClickListener {
            store.accept(mapper.mapUiEvent(PeopleListEventUiElm.ClickedOnSearchIcon))
        }
        searchQueryPublisher.debounce(SEARCH_DEBOUNCE_RATE_MS).onEach {
            store.accept(mapper.mapUiEvent(PeopleListEventUiElm.ChangedSearchQuery(it)))
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun setupDelegateAdapter(store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>): DiffDelegationAdapter {
        return setupDiffDelegatesAdapter(
            PeopleAdapterDelegate(
                onPersonClickListener = {
                    store.accept(mapper.mapUiEvent(PeopleListEventUiElm.ClickedOnPerson(it.id)))
                }
            )
        )
    }

}
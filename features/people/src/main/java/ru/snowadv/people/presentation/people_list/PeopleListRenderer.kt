package ru.snowadv.people.presentation.people_list

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.people.R
import ru.snowadv.people.presentation.adapter.PeopleAdapterDelegate
import ru.snowadv.people.databinding.FragmentPeopleBinding
import ru.snowadv.people.presentation.people_list.elm.PeopleListEffectElm
import ru.snowadv.people.presentation.people_list.elm.PeopleListEventElm
import ru.snowadv.people.presentation.people_list.elm.PeopleListStateElm
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.setupDiffDelegatesAdapter
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.recycler.setupDecorator
import ru.snowadv.presentation.view.setTextIfChanged
import vivid.money.elmslie.core.store.Store

internal class PeopleListRenderer :
    ElmFragmentRenderer<PeopleFragment, FragmentPeopleBinding, PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm> {

    companion object {
        const val SEARCH_DEBOUNCE_RATE_MS = 200L
    }

    private var _adapter: DiffDelegationAdapter? = null
    private val adapter get() = requireNotNull(_adapter) { "Adapter wasn't initialized in PeopleListFragment" }
    private val searchQueryPublisher =
        MutableSharedFlow<String>(extraBufferCapacity = 1) // This is used to debounce search

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
        stateBox.inflateState(state.screenState, R.layout.fragment_people_shimmer)
        adapter.submitList(state.screenState.getCurrentData())
        searchBar.searchEditText.setTextIfChanged(state.searchQuery)
    }

    override fun PeopleFragment.handleEffectByRenderer(
        effect: PeopleListEffectElm,
        binding: FragmentPeopleBinding,
        store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>
    ) {
        when(effect) {
            PeopleListEffectElm.FocusOnSearchFieldAndOpenKeyboard -> focusOnSearchFieldAndOpenKeyboard()
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
            store.accept(PeopleListEventElm.Ui.ClickedOnRetry)
        }
        binding.searchBar.searchIcon.setOnClickListener {
            store.accept(PeopleListEventElm.Ui.ClickedOnSearchIcon)
        }
        searchQueryPublisher.debounce(SEARCH_DEBOUNCE_RATE_MS).onEach {
            store.accept(PeopleListEventElm.Ui.ChangedSearchQuery(it))
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun setupDelegateAdapter(store: Store<PeopleListEventElm, PeopleListEffectElm, PeopleListStateElm>): DiffDelegationAdapter {
        return setupDiffDelegatesAdapter(
            PeopleAdapterDelegate(
                onPersonClickListener = {
                    store.accept(PeopleListEventElm.Ui.ClickedOnPerson(it.id))
                }
            )
        )
    }

}
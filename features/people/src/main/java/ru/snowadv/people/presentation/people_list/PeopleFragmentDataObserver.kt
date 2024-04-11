package ru.snowadv.people.presentation.people_list

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.people.R
import ru.snowadv.people.presentation.adapter.PeopleAdapterDelegate
import ru.snowadv.people.databinding.FragmentPeopleBinding
import ru.snowadv.people.presentation.people_list.event.PeopleListEvent
import ru.snowadv.people.presentation.people_list.event.PeopleListFragmentEvent
import ru.snowadv.people.presentation.people_list.state.PeopleListScreenState
import ru.snowadv.people.presentation.people_list.view_model.PeopleListViewModel
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.setupDiffDelegatesAdapter
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.recycler.setupDecorator
import ru.snowadv.presentation.view.setTextIfChanged
import ru.snowadv.presentation.view.setVisibility

internal class PeopleFragmentDataObserver :
    FragmentDataObserver<FragmentPeopleBinding, PeopleListViewModel, PeopleFragment> {

    override fun PeopleFragment.registerObservingFragment(
        binding: FragmentPeopleBinding,
        viewModel: PeopleListViewModel
    ) {
        val adapter = setupDelegateAdapter(viewModel)
        binding.peopleRecycler.setupDecorator(
            horizontalSpacingResId = ru.snowadv.presentation.R.dimen.common_padding,
            verticalSpacingResId = R.dimen.people_vertical_padding,
        )
        binding.peopleRecycler.adapter = adapter
        observeState(binding, viewModel, adapter)
        observeEvents(viewModel)
        initListeners(binding, viewModel)
    }

    private fun initListeners(
        binding: FragmentPeopleBinding,
        viewModel: PeopleListViewModel,
    ) {
        binding.searchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let { text ->
                viewModel.searchPublisher.tryEmit(text)
            }
        }
        binding.stateBox.setOnRetryClickListener {
            viewModel.handleEvent(PeopleListEvent.ClickedOnRetry)
        }
        binding.searchBar.searchIcon.setOnClickListener {
            viewModel.handleEvent(PeopleListEvent.ClickedOnSearchIcon)
        }
    }

    private fun PeopleFragment.observeEvents(
        viewModel: PeopleListViewModel,
    ) {
        viewModel.eventFlow.onEach {
            handleEvent(it)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun PeopleFragment.handleEvent(
        event: PeopleListFragmentEvent
    ) {
        when(event) {
            PeopleListFragmentEvent.FocusOnSearchFieldAndOpenKeyboard -> focusOnSearchFieldAndOpenKeyboard()
        }
    }

    private fun PeopleFragment.observeState(
        binding: FragmentPeopleBinding,
        viewModel: PeopleListViewModel,
        adapter: DiffDelegationAdapter
    ) {
        viewModel.state.combine(viewModel.searchPublisher) { state, searchQuery ->
            render(binding, state, adapter, searchQuery)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(lifecycleScope)
    }

    private fun render(
        binding: FragmentPeopleBinding,
        state: PeopleListScreenState,
        adapter: DiffDelegationAdapter,
        searchQuery: String,
    ) = with(binding) {
        stateBox.inflateState(state.screenState, R.layout.fragment_people_shimmer)
        adapter.submitList(state.screenState.getCurrentData())
        searchBar.searchEditText.setTextIfChanged(searchQuery)
    }

    private fun setupDelegateAdapter(viewModel: PeopleListViewModel): DiffDelegationAdapter {
        return setupDiffDelegatesAdapter(
            PeopleAdapterDelegate(
                onPersonClickListener = {
                    viewModel.handleEvent(PeopleListEvent.ClickedOnPerson(it.id))
                }
            )
        )
    }

}
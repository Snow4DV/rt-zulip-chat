package ru.snowadv.people.presentation.people_list

import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.snowadv.people.R
import ru.snowadv.people.presentation.adapter.PeopleAdapterDelegate
import ru.snowadv.people.databinding.FragmentPeopleBinding
import ru.snowadv.people.presentation.people_list.event.PeopleListEvent
import ru.snowadv.people.presentation.people_list.state.PeopleListScreenState
import ru.snowadv.people.presentation.people_list.view_model.PeopleListViewModel
import ru.snowadv.presentation.adapter.impl.DiffDelegationAdapter
import ru.snowadv.presentation.adapter.setupDiffDelegatesAdapter
import ru.snowadv.presentation.fragment.FragmentDataObserver
import ru.snowadv.presentation.fragment.inflateState
import ru.snowadv.presentation.fragment.setOnRetryClickListener
import ru.snowadv.presentation.recycler.setupDecorator
import ru.snowadv.presentation.recycler.setupDefaultDecorator

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
        initListeners(binding, viewModel)
    }

    private fun initListeners(
        binding: FragmentPeopleBinding,
        viewModel: PeopleListViewModel,
    ) {
        binding.searchBar.searchEditText.addTextChangedListener { editable ->
            editable?.toString()?.let { text ->
                if (viewModel.state.value.searchQuery != text) {
                    viewModel.handleEvent(PeopleListEvent.ChangedSearchQuery(text))
                }
            }
        }
        binding.stateBox.setOnRetryClickListener {
            viewModel.handleEvent(PeopleListEvent.ClickedOnRetry)
        }
    }

    private fun PeopleFragment.observeState(
        binding: FragmentPeopleBinding,
        viewModel: PeopleListViewModel,
        adapter: DiffDelegationAdapter
    ) {
        viewModel.state.onEach {
            bindState(binding, it, adapter)
        }.flowWithLifecycle(viewLifecycleOwner.lifecycle).launchIn(lifecycleScope)
    }

    private fun bindState(
        binding: FragmentPeopleBinding,
        state: PeopleListScreenState,
        adapter: DiffDelegationAdapter,
    ) = with(binding) {
        stateBox.inflateState(state.filteredScreenState())
        if (state.searchQuery != searchBar.searchEditText.text.toString()) {
            searchBar.searchEditText.setText(state.searchQuery)
        }
        adapter.submitList(state.filteredScreenState().getCurrentData())
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
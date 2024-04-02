package ru.snowadv.people.presentation.people_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.snowadv.people.R
import ru.snowadv.people.databinding.FragmentPeopleBinding
import ru.snowadv.people.di.PeopleGraph
import ru.snowadv.people.domain.navigation.PeopleRouter
import ru.snowadv.people.presentation.people_list.view_model.PeopleListViewModel
import ru.snowadv.people.presentation.people_list.view_model.PeopleListViewModelFactory
import ru.snowadv.presentation.fragment.FragmentDataObserver

class PeopleFragment : Fragment(),
    FragmentDataObserver<FragmentPeopleBinding, PeopleListViewModel, PeopleFragment> by PeopleFragmentDataObserver() {

    companion object {
        fun newInstance(): Fragment = PeopleFragment()
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PeopleListViewModel by viewModels { PeopleListViewModelFactory(PeopleGraph.router) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPeopleBinding.inflate(inflater, container, false).also { _binding = it }.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        registerObservingFragment(binding, viewModel)
    }
}
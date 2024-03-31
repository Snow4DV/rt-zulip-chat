package ru.snowadv.home.presentation.people_list

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.snowadv.home.R
import ru.snowadv.home.databinding.FragmentChannelListBinding
import ru.snowadv.home.databinding.FragmentPeopleBinding
import ru.snowadv.home.presentation.di.HomeGraph
import ru.snowadv.home.presentation.navigation.HomeRouter
import ru.snowadv.home.presentation.people_list.view_model.PeopleListViewModel
import ru.snowadv.home.presentation.people_list.view_model.PeopleListViewModelFactory
import ru.snowadv.presentation.fragment.FragmentDataObserver

class PeopleFragment : Fragment(),
    FragmentDataObserver<FragmentPeopleBinding, PeopleListViewModel, PeopleFragment> by PeopleFragmentDataObserver() {

    companion object {
        fun newInstance(): Fragment = PeopleFragment()
    }

    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PeopleListViewModel by viewModels { PeopleListViewModelFactory(HomeGraph.router) }

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
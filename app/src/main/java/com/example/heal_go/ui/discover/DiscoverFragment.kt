package com.example.heal_go.ui.discover

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentDiscoverBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.adapter.DiscoverAdapter
import com.example.heal_go.ui.dashboard.adapter.LoadingStateAdapter
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory

class DiscoverFragment : Fragment() {

    private val onBoardingViewModel by viewModels<OnboardingViewModel> {
        OnboardingViewModelFactory(OnboardingRepository(requireContext()))
    }
    private val dashboardViewModel by viewModels<DashboardViewModel> {
        ViewModelFactory(
            requireContext()
        )
    }
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

    private var searchDestination = ""
    private var category: String? = null

    private lateinit var adapter: DiscoverAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                changeDiscoverRequest()
                view?.let { requireActivity().hideKeyboard(it) }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        setAdapter()
        changeDiscoverRequest()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            chipAll.setOnClickListener { changeDiscoverRequest() }
            chipPantai.setOnClickListener { changeDiscoverRequest() }
            chipGunung.setOnClickListener { changeDiscoverRequest() }
            chipTaman.setOnClickListener { changeDiscoverRequest() }
            chipCandi.setOnClickListener { changeDiscoverRequest() }
            chipAir.setOnClickListener { changeDiscoverRequest() }
            chipMuseum.setOnClickListener { changeDiscoverRequest() }
            chipPasar.setOnClickListener { changeDiscoverRequest() }
        }
    }

    private fun changeDiscoverRequest() {
        with(binding) {
            category = when {
                chipPantai.isChecked -> "Pantai"
                chipGunung.isChecked -> "Gunung"
                chipTaman.isChecked -> "Taman"
                chipCandi.isChecked -> "Candi"
                chipAir.isChecked -> "Air Terjun"
                chipMuseum.isChecked -> "Museum"
                chipPasar.isChecked -> "Pasar"
                else -> ""
            }

            searchDestination = svSearch.query.toString()

            onBoardingViewModel.getOnboardingDatastore().observe(viewLifecycleOwner) { session ->
                if (session.sessions.data?.token != "" || session.sessions.data?.token != null) {
                    session.sessions.data?.token?.let { token ->
                        if (category.isNullOrEmpty()) category = null

                        dashboardViewModel.getDataDiscover(token, searchDestination, category)
                            .observe(viewLifecycleOwner) {
                                adapter.submitData(lifecycle, it)
                            }
                    }
                }
            }
        }
    }

    private fun setAdapter() {
        val linearLayoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvDestination.layoutManager = linearLayoutManager

        adapter = DiscoverAdapter()
        binding.rvDestination.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
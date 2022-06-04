package com.example.heal_go.ui.discover

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heal_go.data.network.response.DiscoverItem
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentDiscoverBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.adapter.DiscoverAdapter
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

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

    private lateinit var searchDestination: String
    private lateinit var category: String
    private var discoverItem = ArrayList<DiscoverItem>()
    private lateinit var adapter: DiscoverAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)

        binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(requireContext(), query, Toast.LENGTH_SHORT).show()
                view?.let { requireActivity().hideKeyboard(it) }
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return false
            }

        })

        binding.chipgroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val count = group.childCount
            var i = 0

            while (i < count) {
                if ((group.getChildAt(i) as Chip).isChecked) {
                    category = (group.getChildAt(i) as Chip).text.toString()
                }
                i += 1
            }
        }

        /*dibawa ke sini error*/
        Toast.makeText(requireContext(), category, Toast.LENGTH_LONG).show()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBoardingViewModel.getOnboardingDatastore().observe(viewLifecycleOwner) { session ->
            if (session.sessions.data?.token != "" || session.sessions.data?.token != null) {
                session.sessions.data?.token?.let {
                    setAdapter(it)
                }
            }
        }
    }

    private fun setAdapter(token: String) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvDestination.layoutManager = linearLayoutManager

        adapter = DiscoverAdapter()
        binding.rvDestination.adapter = adapter

        dashboardViewModel.getDataDiscover(token, searchDestination, category)
            .observe(viewLifecycleOwner) {
                adapter.submitData(lifecycle, it)
            }
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
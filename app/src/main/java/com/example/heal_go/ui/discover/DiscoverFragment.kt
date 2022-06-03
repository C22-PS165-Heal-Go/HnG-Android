package com.example.heal_go.ui.discover

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heal_go.data.network.response.DestinationItem
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentDiscoverBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.adapter.DestinationAdapter
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory
import com.example.heal_go.util.Status

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

    private var destinationItem = ArrayList<DestinationItem>()
    private lateinit var adapter: DestinationAdapter

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBoardingViewModel.getOnboardingDatastore().observe(viewLifecycleOwner) { session ->
            if (session.sessions.data?.token != "" || session.sessions.data?.token != null) {
                session.sessions.data?.token?.let { dashboardViewModel.getAllDestinations(it) }
            }
        }

        dashboardViewModel.destinations.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Status.Loading -> {}
                is Status.Success -> {
                    if (result.data?.code != null) {
                        Toast.makeText(requireContext(), "Request Failed!", Toast.LENGTH_LONG).show()
                    } else {
                        if (result.data?.success == true) {
                            for (i in 0 until result.data.data?.size!!) {
                                destinationItem.add(result.data?.data[i])
                            }
                        }
                    }
                }
                is Status.Error -> {
                    Toast.makeText(requireContext(), "Sorry, ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }

        setAdapter(destinationItem)
    }

    private fun setAdapter(destinationItem: ArrayList<DestinationItem>) {
        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvDestination.layoutManager = linearLayoutManager

        adapter = DestinationAdapter(destinationItem, false)
        binding.rvDestination.adapter = adapter
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
package com.example.heal_go.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DestinationItem
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentHomeBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.adapter.DestinationAdapter
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory
import com.example.heal_go.ui.questionnaire.QuestionnaireActivity
import com.example.heal_go.util.Status

import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: DestinationAdapter
    private val dashboardViewModel by viewModels<DashboardViewModel> {
        ViewModelFactory(
            requireContext()
        )
    }
    private val onBoardingViewModel by viewModels<OnboardingViewModel> {
        OnboardingViewModelFactory(OnboardingRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.startBtn.setOnClickListener {
            val intent = Intent(activity, QuestionnaireActivity::class.java)
            startActivity(intent)
        }

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
                        Toast.makeText(
                            requireContext(),
                            requireContext().getString(R.string.request_failed),
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        if (result.data?.success == true) {
                            result.data?.data?.let { setAdapter(it) }
                        }
                    }
                }
                is Status.Error -> {
                    Toast.makeText(
                        requireContext(),
                        requireContext().getString(R.string.request_error, result.error),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        return binding.root
    }

    private fun setAdapter(destinationItem: ArrayList<DestinationItem>) {
        val linearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDestination.layoutManager = linearLayoutManager

        adapter = DestinationAdapter(destinationItem)
        binding.rvDestination.adapter = adapter
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
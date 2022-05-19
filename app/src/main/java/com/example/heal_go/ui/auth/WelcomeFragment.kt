package com.example.heal_go.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.heal_go.R
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentWelcomeBinding
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding get() = _binding!!

    private val onboardingViewModel by viewModels<OnboardingViewModel> {
        OnboardingViewModelFactory(OnboardingRepository(requireContext()))
    }

    private val navController: NavController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        onboardingViewModel.getOnboardingDatastore().observe(viewLifecycleOwner) {
            if (it) {
                navController.navigate(R.id.welcomeFragment_to_loginFragment)
            } else {
                navController.navigate(R.id.welcomeFragment_to_onboardingPagerFragment2)
            }
        }


        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
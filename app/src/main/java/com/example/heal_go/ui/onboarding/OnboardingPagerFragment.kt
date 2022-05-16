package com.example.heal_go.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heal_go.databinding.FragmentOnboardingPagerBinding
import com.example.heal_go.ui.onboarding.adapter.OnboardingPagerAdapter
import com.example.heal_go.ui.onboarding.screens.FirstScreen
import com.example.heal_go.ui.onboarding.screens.SecondScreen
import com.example.heal_go.ui.onboarding.screens.ThirdScreen

class OnboardingPagerFragment : Fragment() {

    private var _binding: FragmentOnboardingPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentOnboardingPagerBinding.inflate(inflater, container, false)

        val fragmentList = arrayListOf(
            FirstScreen(),
            SecondScreen(),
            ThirdScreen()
        )

        val adapter = OnboardingPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.onboardingViewpager.adapter = adapter

        return binding.root
    }

}
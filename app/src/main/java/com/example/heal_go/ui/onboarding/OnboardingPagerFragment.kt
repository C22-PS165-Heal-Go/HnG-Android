package com.example.heal_go.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.heal_go.R
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentOnboardingPagerBinding
import com.example.heal_go.ui.onboarding.adapter.OnboardingPagerAdapter
import com.example.heal_go.ui.onboarding.screens.FirstScreen
import com.example.heal_go.ui.onboarding.screens.SecondScreen
import com.example.heal_go.ui.onboarding.screens.ThirdScreen
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory

class OnboardingPagerFragment : Fragment() {

    private var _binding: FragmentOnboardingPagerBinding? = null
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

        _binding = FragmentOnboardingPagerBinding.inflate(inflater, container, false)

        val onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateFooterButton(position)
            }
        }

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

        binding.apply {
            onboardingViewpager.adapter = adapter
            onboardingViewpager.registerOnPageChangeCallback(onBoardingPageChangeCallback)
        }

        return binding.root
    }

    private fun updateFooterButton(position: Int) {
        when (position) {
            0 -> {
                binding.apply {
                    skipBtn.visibility = View.VISIBLE
                    nextBtn.visibility = View.VISIBLE
                    signupBtn.visibility = View.INVISIBLE

                    nextBtn.setOnClickListener {
                        onboardingViewpager.currentItem = 1
                    }

                    skipBtn.setOnClickListener {
                        onboardingViewpager.currentItem = 2
                    }

                    ivFirstCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle_active))
                    ivSecondCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle))
                    ivThirdCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle))
                }
            }
            1 -> {
                binding.apply {
                    skipBtn.visibility = View.VISIBLE
                    nextBtn.visibility = View.VISIBLE
                    signupBtn.visibility = View.INVISIBLE

                    nextBtn.setOnClickListener {
                        onboardingViewpager.currentItem = 2
                    }

                    skipBtn.setOnClickListener {
                        onboardingViewpager.currentItem = 2
                    }

                    ivSecondCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle_active))
                    ivFirstCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle))
                    ivThirdCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle))
                }
            }
            2 -> {
                binding.apply {
                    skipBtn.visibility = View.INVISIBLE
                    nextBtn.visibility = View.INVISIBLE
                    signupBtn.visibility = View.VISIBLE

                    signupBtn.setOnClickListener {
                        navController.navigate(R.id.onboardingPagerFragment_to_registerFragment)
                        onboardingViewModel.onBoardingFinish()
                    }

                    ivThirdCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle_active))
                    ivSecondCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle))
                    ivFirstCircle.setImageDrawable(requireActivity().getDrawable(R.drawable.onboarding_circle))
                }

            }
        }
    }

}
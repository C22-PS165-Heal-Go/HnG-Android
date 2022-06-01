package com.example.heal_go.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heal_go.R
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.FragmentProfileBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
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
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.apply {
            editBtn.setOnClickListener {
                showWarningToast()
            }
            faqBtn.setOnClickListener {
                showWarningToast()
            }
            languageBtn.setOnClickListener {
                showWarningToast()
            }
            rateBtn.setOnClickListener {
                showWarningToast()
            }
            logoutBtn.setOnClickListener {
                onBoardingViewModel.clearLoginSession()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBoardingViewModel.getOnboardingDatastore().observe(requireActivity()) {
            it.sessions.data?.user?.name?.let { name -> setProfile(name) }
        }
    }

    //    Set profile name & temporary profile pict
    private fun setProfile(name: String) {
        Glide.with(requireView())
            .load("https://avatars.dicebear.com/api/adventurer-neutral/${name}.jpg")
            .apply(RequestOptions.placeholderOf(R.drawable.profile_pict_loading).error(R.drawable.image_error_state))
            .into(binding.profilePict)

        binding.tvName.text = name
    }

    private fun showWarningToast() {
        Toast.makeText(
            requireContext(),
            "Sorry but this feature currently not available yet \uD83D\uDE14",
            Toast.LENGTH_SHORT
        ).show()
    }

}
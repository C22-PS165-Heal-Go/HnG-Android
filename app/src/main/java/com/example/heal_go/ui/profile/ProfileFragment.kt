package com.example.heal_go.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentHomeBinding
import com.example.heal_go.databinding.FragmentProfileBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val dashboardViewModel by viewModels<DashboardViewModel> { ViewModelFactory(requireContext()) }

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
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProfileImage()
    }

    private fun setProfileImage() {
        Glide.with(requireView())
            .load("https://avatars.dicebear.com/api/adventurer-neutral/healgo.jpg")
            .into(binding.profilePict)
    }

    private fun showWarningToast() {
        Toast.makeText(requireContext(), "Sorry but this feature currently not available yet \uD83D\uDE14", Toast.LENGTH_SHORT).show()
    }

}
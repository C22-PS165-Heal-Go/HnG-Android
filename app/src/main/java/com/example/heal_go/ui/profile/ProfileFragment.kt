package com.example.heal_go.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.heal_go.R
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel

class ProfileFragment : Fragment() {

    private val dashboardViewModel by viewModels<DashboardViewModel> { ViewModelFactory(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

}
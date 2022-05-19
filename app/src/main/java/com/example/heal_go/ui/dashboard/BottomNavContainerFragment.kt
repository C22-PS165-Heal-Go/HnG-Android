package com.example.heal_go.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentBottomNavContainerBinding
import com.example.heal_go.ui.auth.LoginFragment
import com.example.heal_go.ui.auth.RegisterFragment
import com.example.heal_go.ui.dashboard.adapter.DashboardPagerAdapter
import com.example.heal_go.ui.discover.DiscoverFragment
import com.example.heal_go.ui.home.HomeFragment
import com.example.heal_go.ui.profile.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavContainerFragment : Fragment() {

    private var _binding: FragmentBottomNavContainerBinding? = null
    private val binding get() = _binding!!

    private val navController: NavController by lazy {
        findNavController()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        _binding = FragmentBottomNavContainerBinding.inflate(inflater, container, false)

        val onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                onScreenChanged(position)
                requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
                    onBackpressed(position)
                }
            }
        }

        val fragmentList = arrayListOf(
            HomeFragment(),
            DiscoverFragment(),
            ProfileFragment()
        )

        val adapter = DashboardPagerAdapter(
            fragmentList,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.apply {
            dashboardViewpager.adapter = adapter
            dashboardViewpager.registerOnPageChangeCallback(onBoardingPageChangeCallback)
            dashboardViewpager.isUserInputEnabled = false

            bottomnavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        }

        return binding.root
    }

    private fun onScreenChanged(position: Int) {
        when (position) {
            0 -> {
                binding.bottomnavView.selectedItemId = R.id.navigation_home
            }
            1 -> {
                binding.bottomnavView.selectedItemId = R.id.navigation_discover
            }
            2 -> {
                binding.bottomnavView.selectedItemId = R.id.navigation_profile
            }
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.navigation_home -> {
                    binding.dashboardViewpager.currentItem = 0
                }
                R.id.navigation_discover -> {
                    binding.dashboardViewpager.currentItem = 1
                }
                R.id.navigation_profile -> {
                    binding.dashboardViewpager.currentItem = 2
                }
            }
            return@OnNavigationItemSelectedListener true
        }

    private fun onBackpressed(position: Int) {
        when (position) {
            0 -> {
                requireActivity().finish()
            }
            1 -> {
                binding.dashboardViewpager.currentItem = 0
            }
            2 -> {
                binding.dashboardViewpager.currentItem = 0
            }
        }
    }

}
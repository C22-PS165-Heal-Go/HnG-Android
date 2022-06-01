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
import com.example.heal_go.databinding.FragmentDiscoverBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.viewmodel.DashboardViewModel

class DiscoverFragment : Fragment() {

    private val dashboardViewModel by viewModels<DashboardViewModel> {
        ViewModelFactory(
            requireContext()
        )
    }
    private var _binding: FragmentDiscoverBinding? = null
    private val binding get() = _binding!!

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
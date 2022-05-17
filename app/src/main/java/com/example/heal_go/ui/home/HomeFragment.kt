package com.example.heal_go.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentFirstScreenBinding
import com.example.heal_go.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.startBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Start now", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}
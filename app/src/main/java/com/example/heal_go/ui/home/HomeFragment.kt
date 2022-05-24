package com.example.heal_go.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.heal_go.databinding.FragmentHomeBinding
import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.chipgroup.children.toList().filter { (it as Chip).isChecked }
            .forEach { Log.d("COBA", it.toString()) }

        binding.startBtn.setOnClickListener {
            Toast.makeText(requireContext(), "Start now", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

}
package com.example.heal_go.ui.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.example.heal_go.databinding.FragmentHomeBinding
import com.example.heal_go.ui.questionnaire.QuestionnaireActivity

import com.google.android.material.chip.Chip

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.chipgroup.children.toList().filter { (it as Chip).isChecked }
            .forEach { Log.d("COBA", it.toString()) }

        binding.startBtn.setOnClickListener {
            val intent = Intent(activity, QuestionnaireActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}
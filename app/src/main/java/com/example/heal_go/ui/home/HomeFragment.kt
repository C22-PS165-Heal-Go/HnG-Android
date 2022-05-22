package com.example.heal_go.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.heal_go.databinding.FragmentHomeBinding
import com.example.heal_go.ui.questionnaire.QuestionnaireActivity


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.startBtn.setOnClickListener {
            val intent = Intent(activity, QuestionnaireActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

}
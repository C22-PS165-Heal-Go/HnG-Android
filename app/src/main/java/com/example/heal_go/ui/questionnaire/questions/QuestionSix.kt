package com.example.heal_go.ui.questionnaire.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentQuestionFiveBinding
import com.example.heal_go.databinding.FragmentQuestionSixBinding


class QuestionSix : Fragment() {

    private var _binding: FragmentQuestionSixBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionSixBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}
package com.example.heal_go.ui.questionnaire.questions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.heal_go.R
import com.example.heal_go.databinding.FragmentQuestionFiveBinding
import com.example.heal_go.databinding.FragmentQuestionTwoBinding
import com.example.heal_go.ui.questionnaire.viewmodel.QuestionnaireViewModel

class QuestionFive : Fragment() {

    private var _binding: FragmentQuestionFiveBinding? = null
    private val binding get() = _binding!!

    private lateinit var questionnaireViewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionFiveBinding.inflate(inflater, container, false)

        questionnaireViewModel =
            ViewModelProvider(requireActivity())[QuestionnaireViewModel::class.java]

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            setChoice()
        }

        return binding.root
    }

    private fun setChoice() {

        binding.apply {
            when (radioGroup.checkedRadioButtonId) {
                item1.id -> {
                    questionnaireViewModel.saveChoice(questionFive = 0)
                }
                item2.id -> {
                    questionnaireViewModel.saveChoice(questionFive = 1)
                }
                item3.id -> {
                    questionnaireViewModel.saveChoice(questionFive = 2)
                }
                item4.id -> {
                    questionnaireViewModel.saveChoice(questionFive = 3)
                }
                item5.id -> {
                    questionnaireViewModel.saveChoice(questionFive = 4)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }
}
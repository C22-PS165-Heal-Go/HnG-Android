package com.example.heal_go.ui.questionnaire.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.heal_go.databinding.FragmentQuestionTwoBinding
import com.example.heal_go.ui.questionnaire.viewmodel.QuestionnaireViewModel

class QuestionTwo : Fragment() {

    private var _binding: FragmentQuestionTwoBinding? = null
    private val binding get() = _binding!!

    private val itemList = ArrayList<Int>()

    private lateinit var questionnaireViewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionTwoBinding.inflate(inflater, container, false)

        questionnaireViewModel =
            ViewModelProvider(requireActivity())[QuestionnaireViewModel::class.java]

        binding.apply {
            item1.setOnClickListener {
                questionnaireViewModel.saveChoice(questionTwo = setChoice(0))
            }
            item2.setOnClickListener {
                questionnaireViewModel.saveChoice(questionTwo = setChoice(1))
            }
            item3.setOnClickListener {
                questionnaireViewModel.saveChoice(questionTwo = setChoice(2))
            }
            item4.setOnClickListener {
                questionnaireViewModel.saveChoice(questionTwo = setChoice(3))
            }
            item5.setOnClickListener {
                questionnaireViewModel.saveChoice(questionTwo = setChoice(4))
            }
        }

        return binding.root
    }

    private fun setChoice(id: Int): List<Int> {
        if (itemList.contains(id)) {
            itemList.remove(id)
        } else {
            itemList.add(id)
        }
        return itemList.toList()
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

}
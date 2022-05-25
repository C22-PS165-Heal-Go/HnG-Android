package com.example.heal_go.ui.questionnaire.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.heal_go.databinding.FragmentQuestionThreeBinding
import com.example.heal_go.ui.questionnaire.viewmodel.QuestionnaireViewModel

class QuestionThree : Fragment() {

    private var _binding: FragmentQuestionThreeBinding? = null
    private val binding get() = _binding!!

    private val itemList = ArrayList<Int>()

    private lateinit var questionnaireViewModel: QuestionnaireViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuestionThreeBinding.inflate(inflater, container, false)

        questionnaireViewModel =
            ViewModelProvider(requireActivity())[QuestionnaireViewModel::class.java]

        binding.apply {
            item1.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(0))
            }
            item2.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(1))
            }
            item3.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(2))
            }
            item4.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(3))
            }
            item5.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(4))
            }
            item6.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(5))
            }
            item7.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(6))
            }
            item8.setOnClickListener {
                questionnaireViewModel.saveChoice(questionThree = setChoice(7))
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
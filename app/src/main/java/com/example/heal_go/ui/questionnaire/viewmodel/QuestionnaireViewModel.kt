package com.example.heal_go.ui.questionnaire.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.heal_go.data.QuestionnaireReqBody
import com.example.heal_go.data.repository.MainRepository

class QuestionnaireViewModel(provideMainRepository: MainRepository, context: Context) : ViewModel() {

    private val _quesionnaireAnswer = MutableLiveData<QuestionnaireReqBody>()
    val quesionnaireAnswer: LiveData<QuestionnaireReqBody> = _quesionnaireAnswer

    fun saveChoice(
        questionOne: Int? = null,
        questionTwo: List<Int>? = null,
        questionThree: List<Int>? = null,
        questionFour: Int? = null,
        questionFive: Int? = null,
        questionSix: Int? = null,
        questionSeven: Int? = null,
        questionEight: Int? = null
    ) {
        _quesionnaireAnswer.value = QuestionnaireReqBody(
            questionSeven ?: _quesionnaireAnswer.value?.question7,
            questionEight ?: _quesionnaireAnswer.value?.question8,
            questionSix ?: _quesionnaireAnswer.value?.question6,
            questionThree ?: _quesionnaireAnswer.value?.question3,
            questionTwo ?: _quesionnaireAnswer.value?.question2,
            questionFive ?: _quesionnaireAnswer.value?.question5,
            questionFour ?: _quesionnaireAnswer.value?.question4,
            questionOne ?: _quesionnaireAnswer.value?.question1
        )
    }

}
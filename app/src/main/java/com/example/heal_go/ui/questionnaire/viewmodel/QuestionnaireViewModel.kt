package com.example.heal_go.ui.questionnaire.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.heal_go.R
import com.example.heal_go.data.QuestionnaireReqBody
import com.example.heal_go.data.network.response.RecommendationResponse
import com.example.heal_go.data.repository.MainRepository
import com.example.heal_go.util.Status
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class QuestionnaireViewModel(provideMainRepository: MainRepository, private val context: Context) :
    ViewModel() {

    private val repository = provideMainRepository

    private val _quesionnaireAnswer = MutableLiveData<QuestionnaireReqBody>()
    val quesionnaireAnswer: LiveData<QuestionnaireReqBody> = _quesionnaireAnswer

    private val _response = MutableLiveData<Status<RecommendationResponse?>>()
    val response: LiveData<Status<RecommendationResponse?>> get() = _response

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

    fun sendQuestionnaire(token: String) {
        _response.value = Status.Loading(true)

        val questionOne = quesionnaireAnswer.value?.question1!!
        val questionTwo = quesionnaireAnswer.value?.question2!!
        val questionThree = quesionnaireAnswer.value?.question3!!
        val questionFour = quesionnaireAnswer.value?.question4!!
        val questionFive = quesionnaireAnswer.value?.question5!!
        val questionSix = quesionnaireAnswer.value?.question6!!
        val questionSeven = quesionnaireAnswer.value?.question7!!
        val questionEight = quesionnaireAnswer.value?.question8!!


        try {
            viewModelScope.launch {
                val response = repository.getRecommendation(
                    token,
                    questionOne,
                    questionTwo as List<Int>,
                    questionThree as List<Int>,
                    questionFour,
                    questionFive,
                    questionSix,
                    questionSeven,
                    questionEight
                )

                _response.value = Status.Success(response)
            }
        }catch (e: Exception) {
            _response.value = Status.Error(context.getString(R.string.server_error))
        } finally {
            _response.value = Status.Loading(false)
        }


    }



}
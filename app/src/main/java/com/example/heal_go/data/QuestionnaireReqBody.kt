package com.example.heal_go.data

import com.google.gson.annotations.SerializedName

data class QuestionnaireReqBody(

	@field:SerializedName("question7")
	val question7: Int? = null,

	@field:SerializedName("question8")
	val question8: Int? = null,

    @field:SerializedName("question6")
    val question6: Int? = null,

    @field:SerializedName("question3")
    val question3: List<Int?>? = null,

    @field:SerializedName("question2")
    val question2: List<Int?>? = null,

    @field:SerializedName("question5")
    val question5: Int? = null,

    @field:SerializedName("question4")
    val question4: Int? = null,

    @field:SerializedName("question1")
    val question1: Int? = null
)

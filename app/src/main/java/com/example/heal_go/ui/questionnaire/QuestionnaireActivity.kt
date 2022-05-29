package com.example.heal_go.ui.questionnaire

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.heal_go.R
import com.example.heal_go.databinding.ActivityQuestionnaireBinding
import com.example.heal_go.ui.onboarding.adapter.OnboardingPagerAdapter
import com.example.heal_go.ui.questionnaire.questions.*
import com.example.heal_go.ui.questionnaire.viewmodel.QuestionnaireViewModel
import com.example.heal_go.ui.recommendation.RecommendationCardActivity

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding

    private val questionnaireViewModel by viewModels<QuestionnaireViewModel>()

    lateinit var dialogBuilder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        initView()
        setContentView(binding.root)

        val onBoardingPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setNextButton(position)
                setEnabledButton(position)
            }
        }

        val fragmentList = arrayListOf(
            QuestionOne(),
            QuestionTwo(),
            QuestionThree(),
            QuestionFour(),
            QuestionFive(),
            QuestionSix(),
            QuestionSeven(),
            QuestionEight()
        )

        val adapter = OnboardingPagerAdapter(
            fragmentList,
            supportFragmentManager,
            lifecycle
        )

        binding.apply {
            questionViewpager.adapter = adapter
            questionViewpager.registerOnPageChangeCallback(onBoardingPageChangeCallback)
            questionViewpager.isUserInputEnabled = false

            nextBtn.setOnClickListener {
                setProgressBar(true)
                binding.questionViewpager.currentItem += 1
            }

            backBtn.setOnClickListener {
                setProgressBar(false)
                binding.questionViewpager.currentItem -= 1
            }

            finishBtn.setOnClickListener {
                showSubmitDialog()
            }
        }

        questionnaireViewModel.quesionnaireAnswer.observe(this) {
            Log.d("REQBODY", it.toString())
        }
    }

    private fun setEnabledButton(position: Int) {
        questionnaireViewModel.quesionnaireAnswer.observe(this@QuestionnaireActivity) {
            when (position) {
                0 -> {
                    binding.nextBtn.isEnabled = it.question1 != null
                }
                1 -> {
                    binding.nextBtn.isEnabled = it.question2 != null && it.question2.isNotEmpty()
                }
                2 -> {
                    binding.nextBtn.isEnabled = it.question3 != null && it.question3.isNotEmpty()
                }
                3 -> {
                    binding.nextBtn.isEnabled = it.question4 != null
                }
                4 -> {
                    binding.nextBtn.isEnabled = it.question5 != null
                }
                5 -> {
                    binding.nextBtn.isEnabled = it.question6 != null
                }
                6 -> {
                    binding.nextBtn.isEnabled = it.question7 != null
                }
                7 -> {
                    binding.finishBtn.isEnabled = it.question8 != null
                }
            }
        }
    }

    private fun setNextButton(position: Int) {
        when (position) {
            0 -> {
                binding.backBtn.visibility = View.INVISIBLE
                binding.finishBtn.visibility = View.GONE
                binding.nextBtn.visibility = View.VISIBLE
            }
            7 -> {
                binding.backBtn.visibility = View.VISIBLE
                binding.finishBtn.visibility = View.VISIBLE
                binding.nextBtn.visibility = View.GONE
            }
            else -> {
                binding.backBtn.visibility = View.VISIBLE
                binding.finishBtn.visibility = View.GONE
                binding.nextBtn.visibility = View.VISIBLE
            }
        }
    }


    private fun setProgressBar(add: Boolean) {

        binding.apply {
            if (add) {
                progressBar.progress = progressBar.progress + 1
            } else {
                progressBar.progress = progressBar.progress - 1
            }
            tvRemainingquestion.text = "Question ${binding.progressBar.progress} / 8"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSubmitDialog() {
        dialogBuilder.setView(layoutInflater.inflate(R.layout.questionnaire_custom_dialog, null))
        dialogBuilder()
    }

    private fun showWarningDialog() {
        dialogBuilder.setView(
            layoutInflater.inflate(
                R.layout.questionnaire_custom_warning_dialog,
                null
            )
        )
        dialogBuilder()
    }

    private fun dialogBuilder() {
        val dialog = dialogBuilder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()

        val btnProceed = dialog.findViewById<Button>(R.id.proceed_btn)
        val btnCancel = dialog.findViewById<Button>(R.id.cancel_btn)
        val btnClose = dialog.findViewById<ImageButton>(R.id.close_btn)

        btnProceed?.setOnClickListener {
            if (btnProceed.text == "Submit") {
                val intent = Intent(this, RecommendationCardActivity::class.java)
                startActivity(intent)
            }
            finish()
        }

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun onBackPressed() {
        if (binding.questionViewpager.currentItem - 1 < 0) {
            showWarningDialog()
        } else {
            binding.questionViewpager.currentItem -= 1
            setProgressBar(false)
        }
    }

    private fun initView() {
        supportActionBar?.hide()
        dialogBuilder =
            AlertDialog.Builder(this@QuestionnaireActivity, R.style.WrapContentDialog)
    }
}
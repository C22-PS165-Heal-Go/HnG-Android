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
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.example.heal_go.R
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.ActivityQuestionnaireBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.onboarding.adapter.OnboardingPagerAdapter
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory
import com.example.heal_go.ui.questionnaire.questions.*
import com.example.heal_go.ui.questionnaire.viewmodel.QuestionnaireViewModel
import com.example.heal_go.ui.recommendation.RecommendationCardActivity
import com.example.heal_go.ui.recommendation.viewmodel.RecommendationViewModel
import com.example.heal_go.util.LoadingDialog
import com.example.heal_go.util.Status

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding

    private val questionnaireViewModel by viewModels<QuestionnaireViewModel>{ ViewModelFactory(applicationContext) }

    private val onBoardingViewModel by viewModels<OnboardingViewModel> {
        OnboardingViewModelFactory(OnboardingRepository(this))
    }

    private val recommendationViewModel by viewModels<RecommendationViewModel> {
        ViewModelFactory(
            this
        )
    }

    /* build loading dialog */
    private lateinit var loadingDialogBuilder: LoadingDialog
    private lateinit var  loadingDialog: AlertDialog

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


        questionnaireViewModel.response.observe(this) { result ->
            when(result) {
                is Status.Loading -> {
                    loadingDialog.show()
                }
                is Status.Success -> {
                    loadingDialog.dismiss()
                    if (result.data?.code != null) {
                        Toast.makeText(this, result.data.message, Toast.LENGTH_SHORT).show()
                    } else {
                        if (result.data?.success == true) {
                            val intent = Intent(this, RecommendationCardActivity::class.java)
                            intent.putExtra(DESTINATION_DATA, result.data)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
                is Status.Error -> {
                    loadingDialog.dismiss()
                    Log.d("RECOMMENDATION", result.error)
                }
            }
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
            tvRemainingquestion.text = getString(R.string.number_of_questions, binding.progressBar.progress)
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
                sendQuestionnaire()
                dialog.dismiss()
            }else {
                finish()
            }
        }

        btnClose?.setOnClickListener {
            dialog.dismiss()
        }

        btnCancel?.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun sendQuestionnaire() {

        onBoardingViewModel.getOnboardingDatastore().observe(this) {
            it.sessions.data?.token?.let { it1 -> questionnaireViewModel.sendQuestionnaire(it1) }
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

    private fun buildLoadingDialog() {
        loadingDialogBuilder = LoadingDialog(this)
        loadingDialog = loadingDialogBuilder.buildLoadingDialog()
    }

    private fun initView() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        supportActionBar?.hide()
        dialogBuilder =
            AlertDialog.Builder(this@QuestionnaireActivity, R.style.WrapContentDialog)
        buildLoadingDialog()
    }

    companion object {
        const val DESTINATION_DATA = "destination_data"
    }
}
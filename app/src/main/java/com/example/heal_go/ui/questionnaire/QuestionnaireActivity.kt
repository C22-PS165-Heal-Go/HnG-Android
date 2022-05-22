package com.example.heal_go.ui.questionnaire

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.heal_go.R
import com.example.heal_go.databinding.ActivityQuestionnaireBinding

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        initView()
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener {
            setProgressBar(true)
        }

        binding.backBtn.setOnClickListener {
            setProgressBar(false)
        }

    }

    private fun setProgressBar(add: Boolean) {

        binding.apply {
            if (add) {
                progressBar.progress = progressBar.progress + 1
            }else {
                if (progressBar.progress == 1) {
                    progressBar.progress = 1
                }else {
                    progressBar.progress = progressBar.progress - 1
                }
            }
            tvRemainingquestion.text = "Question ${binding.progressBar.progress} / 7"
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

    private fun initView() {
        supportActionBar?.elevation = 0F
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Questionnaire"
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_500)
    }
}
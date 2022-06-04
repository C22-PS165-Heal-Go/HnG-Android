package com.example.heal_go.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DestinationItem
import com.example.heal_go.databinding.ActivityDestinationDetailBinding
import com.example.heal_go.ui.dashboard.adapter.DestinationAdapter

class DestinationDetailActivity : AppCompatActivity() {

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityDestinationDetailBinding.inflate(layoutInflater)
    }

    private lateinit var circularProgressDrawable: CircularProgressDrawable

    private var destinationData: DestinationItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        setupContent()
    }

    private fun initView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()

        destinationData = intent.getParcelableExtra(DestinationAdapter.DESTINATION_DETAIL)
    }


    private fun setupContent() {

        circularProgressDrawable = CircularProgressDrawable(this@DestinationDetailActivity)
        circularProgressDrawable.strokeWidth = 8f
        circularProgressDrawable.centerRadius = 40f
        circularProgressDrawable.setColorSchemeColors(R.color.primary_500)
        circularProgressDrawable.start()

        binding.apply {
            tvTitle.text = destinationData?.name
            tvDescription.text = destinationData?.description
            tvLocation.text = destinationData?.location

            Glide.with(this@DestinationDetailActivity)
                .load(destinationData?.image)
                .apply(
                    RequestOptions.placeholderOf(circularProgressDrawable)
                        .error(R.drawable.image_error_state)
                )
                .into(ivDestination)

            binding.backBtn.setOnClickListener {
                onBackPressed()
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}
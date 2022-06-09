package com.example.heal_go.ui.recommendation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DestinationDetail
import com.example.heal_go.data.network.response.RecommendationDataItem
import com.example.heal_go.data.network.response.RecommendationResponse
import com.example.heal_go.data.repository.OnboardingRepository
import com.example.heal_go.databinding.ActivityRecommendationCardBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.DashboardActivity
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModel
import com.example.heal_go.ui.onboarding.viewmodel.OnboardingViewModelFactory
import com.example.heal_go.ui.questionnaire.QuestionnaireActivity
import com.example.heal_go.ui.recommendation.adapter.CardAdapter
import com.example.heal_go.ui.recommendation.viewmodel.RecommendationViewModel
import com.example.heal_go.util.LoadingDialog
import com.example.heal_go.util.Status
import com.yuyakaido.android.cardstackview.*
import org.json.JSONObject

class RecommendationCardActivity : AppCompatActivity(), DetailBottomSheet.OnActionClickListener,
    TutorialBottomSheet.OnActionClickListener {

    private lateinit var listAdapter: CardAdapter
    private lateinit var manager: CardStackLayoutManager
    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityRecommendationCardBinding.inflate(layoutInflater)
    }

    private var destinationData: RecommendationResponse? = null

    private val recommendationViewModel by viewModels<RecommendationViewModel> {
        ViewModelFactory(
            this
        )
    }

    private val onBoardingViewModel by viewModels<OnboardingViewModel> {
        OnboardingViewModelFactory(OnboardingRepository(this))
    }

    private var swipeHistory = ArrayList<JSONObject>()
    private var isClicked = false

    private lateinit var loadingDialogBuilder: LoadingDialog
    private lateinit var loadingDialog: AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupView()
        setCardStackAdapter()
        actions()
        setAnimationsIn()
    }


    /*to confirm what messages from bottom sheet dialog,
    this function can be overrided to process that
    - actions 1 for interested recommendation
    - actions 2 for not-interested recommendation
    */
    override fun onActionClicked(actions: Int) {
        when (actions) {
            1 -> {
                swipeCard(true)
                showLoveIcon()
                Toast.makeText(
                    this@RecommendationCardActivity,
                    getString(R.string.interested),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            2 -> {
                Toast.makeText(
                    this@RecommendationCardActivity,
                    getString(R.string.not_interested),
                    Toast.LENGTH_SHORT
                )
                    .show()
                isClicked = true
                swipeCard(false)
                binding.recycleView.swipe()
            }
        }
    }

    override fun onUnderstandBtnClickListener() {
        recommendationViewModel.onTutorialFinish()
    }

    /* when the card has been processed, swipe that
    * if user is interested with the recommendation, swipe card to bottom
    * if user is not interested, swipe to left
    * */
    private fun swipeCard(interested: Boolean) {
        val setting = SwipeAnimationSetting.Builder().apply {
            setDirection(if (interested) Direction.Bottom else Direction.Left)
            setDuration(Duration.Normal.duration)
            setInterpolator(AccelerateInterpolator())
        }.build()
        manager.setSwipeAnimationSetting(setting)

        /*add swipe result into array*/
        val jsonObject = JSONObject()
        jsonObject.put("id", destinationData?.data?.get(swipeHistory.size)?.id!!)
        jsonObject.put("like", interested)
        swipeHistory.add(jsonObject)

        /*add data into server*/
        if (swipeHistory.size >= destinationData?.data?.size!!) {
            onBoardingViewModel.getOnboardingDatastore().observe(this) { session ->
                if (session.sessions.data?.token != "" || session.sessions.data?.token != null) {
                    session.sessions.data?.token?.let { token ->
                        recommendationViewModel.sendSwipeRecommendation(token, swipeHistory)
                    }
                }
            }

            /*change state from viewmodel*/
            recommendationViewModel.swipeResponse.observe(this) { result ->
                when (result) {
                    is Status.Loading -> {
                        loadingDialog.show()
                    }
                    is Status.Success -> {
                        loadingDialog.dismiss()
                        if (result.data?.code != null) {
                            Toast.makeText(
                                this@RecommendationCardActivity,
                                getString(R.string.request_failed),
                                Toast.LENGTH_SHORT
                            ).show()

                            resetCard()
                        } else {
                            if (result.data?.data?.status != null) {
                                setAnimationsOut()
                            }
                        }
                    }
                    is Status.Error -> {
                        loadingDialog.dismiss()
                        Toast.makeText(
                            this@RecommendationCardActivity,
                            result.error,
                            Toast.LENGTH_SHORT
                        ).show()

                        resetCard()
                    }
                }
            }
        }
    }

    private fun resetCard() {
        for (i in destinationData?.data?.indices!!) {
            binding.recycleView.rewind()
        }
    }

    /*this function is used when the recommendation is up*/
    private fun setAnimationsOut() {
        with(binding) {
            val recyclerView =
                ObjectAnimator.ofFloat(this.recycleView, View.ALPHA, 0f).setDuration(250)
            val actionTab =
                ObjectAnimator.ofFloat(this.actionLayout, View.ALPHA, 0f).setDuration(250)
            val tutorialbtn =
                ObjectAnimator.ofFloat(this.tutorialBtn, View.ALPHA, 0f).setDuration(250)
            val actionLayout =
                ObjectAnimator.ofFloat(this.actionLayout, View.ALPHA, 0f).setDuration(250)

            /*fade out all contents in this fragment*/
            AnimatorSet().apply {
                playTogether(recyclerView, actionTab, tutorialbtn, actionLayout)
                start()
            }

            /*show success animation*/
            this.lavSuccess.apply {
                alpha = 1.0f
                progress = 0.0f
                pauseAnimation()
                playAnimation()
                repeatCount = 0
            }

            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@RecommendationCardActivity, DashboardActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            }, 2000)

        }
    }

    /*this function is used when page load all its contents*/
    private fun setAnimationsIn() {
        val recyclerView =
            ObjectAnimator.ofFloat(binding.recycleView, View.ALPHA, 1f).setDuration(500)
        val actionTab =
            ObjectAnimator.ofFloat(binding.actionLayout, View.ALPHA, 1f).setDuration(500)

        /*fade in all contents*/
        AnimatorSet().apply {
            playSequentially(recyclerView, actionTab)
            startDelay = 1000
            start()
        }
    }

    /*this function is used to define all contents' behaviour in this page*/
    private fun actions() {
        with(binding) {
            /*when rewind is clicked, previous card which has been swiped will be restored*/
            this.rewindBtn.setOnClickListener {
                if (swipeHistory.size > 0) {
                    recycleView.rewind()
                    swipeHistory.removeLast()
                } else {
                    Toast.makeText(
                        this@RecommendationCardActivity,
                        getString(R.string.all_contents_rewinded),
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

            /*when interested button is clicked, current card will be swiped to bottom*/
            this.interestedBtn.setOnClickListener {
                Toast.makeText(
                    this@RecommendationCardActivity,
                    getString(R.string.interested),
                    Toast.LENGTH_SHORT
                )
                    .show()
                swipeCard(true)
                showLoveIcon()
            }

            /*when interested button is clicked, current card will be swiped to left*/
            this.notInterestedBtn.setOnClickListener {
                Toast.makeText(
                    this@RecommendationCardActivity,
                    getString(R.string.not_interested),
                    Toast.LENGTH_SHORT
                )
                    .show()
                isClicked = true
                swipeCard(false)
                recycleView.swipe()
            }

//        CARD ONCLICK LISTENER
            listAdapter.setOnItemClickCallBack(object : CardAdapter.OnItemClickCallBack {
                /*when double click action, current card will be marked as interested*/
                override fun onItemClicked(data: RecommendationDataItem) {
                    Toast.makeText(
                        this@RecommendationCardActivity,
                        getString(R.string.interested),
                        Toast.LENGTH_SHORT
                    ).show()
                    swipeCard(true)
                    showLoveIcon()
                }

                /*when card on hold with user, page will load bottom sheet dialog to show recommendation detail*/
                override fun onHoldClicked(data: DestinationDetail) {
                    val modal = DetailBottomSheet(data)
                    modal.show(supportFragmentManager, modal.tag)
                }
            })

            this.tutorialBtn.setOnClickListener {
                showTutorialBottomSheet()
            }

            recommendationViewModel.getTutorialDatastore()
                .observe(this@RecommendationCardActivity) {
                    if (!it) {
                        Handler(Looper.getMainLooper()).postDelayed({
                            showTutorialBottomSheet()
                        }, 4000)
                    }
                }
        }
    }

    private fun showLoveIcon() {
        binding.apply {
            loveLottie.visibility = View.VISIBLE
            loveLottie.addAnimatorUpdateListener {
                if (it.animatedValue == 1.0f) {
                    loveLottie.visibility = View.GONE
                    if (loveLottie.visibility == View.GONE) {
                        binding.recycleView.swipe()
                    }
                }
            }
        }
    }

    private fun showTutorialBottomSheet() {
        val tutorialBottomSheet = TutorialBottomSheet()
        tutorialBottomSheet.isCancelable = false
        tutorialBottomSheet.show(supportFragmentManager, tutorialBottomSheet.tag)
    }

    /*to define card action, this method implement all of those actions*/
    private fun setCardStackAdapter() {
        //LISTENER WHILE CARD IN INTERACTION
        manager =
            CardStackLayoutManager(this@RecommendationCardActivity, object : CardStackListener {
                override fun onCardDragging(direction: Direction?, ratio: Float) {}
                override fun onCardSwiped(direction: Direction?) {
                    binding.loveLottie.visibility = View.GONE
                    if (direction != Direction.Bottom) {
                        if (!isClicked) {
                            Toast.makeText(
                                this@RecommendationCardActivity,
                                getString(R.string.not_interested),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            swipeCard(false)
                        } else isClicked = false
                    }
                }

                override fun onCardRewound() {}
                override fun onCardCanceled() {}
                override fun onCardAppeared(view: View?, position: Int) {}
                override fun onCardDisappeared(view: View?, position: Int) {}
            })

        val list = ArrayList<RecommendationDataItem>()

        for (i in destinationData?.data?.indices!!) {
            destinationData!!.data?.get(i)?.let { list.add(it) }
        }

        /*load card as stack from bottom and load that into adapter*/
        manager.setStackFrom(StackFrom.Bottom)
        manager.setVisibleCount(list.size)

        binding.recycleView.layoutManager = manager

        listAdapter = CardAdapter(list)
        binding.recycleView.adapter = listAdapter
    }

    private fun setupView() {
        buildLoadingDialog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            this.window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            this.window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        supportActionBar?.hide()
        destinationData = intent.getParcelableExtra(QuestionnaireActivity.DESTINATION_DATA)
    }

    private fun buildLoadingDialog() {
        loadingDialogBuilder = LoadingDialog(this@RecommendationCardActivity)
        loadingDialog = loadingDialogBuilder.buildLoadingDialog()
    }
}
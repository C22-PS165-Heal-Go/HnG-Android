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
import androidx.appcompat.app.AppCompatActivity
import com.example.heal_go.data.network.response.DestinationDetail
import com.example.heal_go.data.network.response.RecommendationDataItem
import com.example.heal_go.data.network.response.RecommendationResponse
import com.example.heal_go.databinding.ActivityRecommendationCardBinding
import com.example.heal_go.ui.ViewModelFactory
import com.example.heal_go.ui.dashboard.DashboardActivity
import com.example.heal_go.ui.questionnaire.QuestionnaireActivity
import com.example.heal_go.ui.recommendation.adapter.CardAdapter
import com.example.heal_go.ui.recommendation.viewmodel.RecommendationViewModel
import com.yuyakaido.android.cardstackview.*

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

    private var countSwipe = 0

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
            1 -> swipeCard(true)
            2 -> swipeCard(false)
        }
        binding.recycleView.swipe()
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

        /*temporary, will be replaced with reinforcement learning*/
        countSwipe++

        if (countSwipe >= 5) setAnimationsOut()
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
                recycleView.rewind()
                countSwipe--
            }

            /*when interested button is clicked, current card will be swiped to bottom*/
            this.interestedBtn.setOnClickListener {
                Toast.makeText(this@RecommendationCardActivity, "Interested", Toast.LENGTH_SHORT)
                    .show()
                swipeCard(true)
                recycleView.swipe()
            }

            /*when interested button is clicked, current card will be swiped to left*/
            this.notInterestedBtn.setOnClickListener {
                Toast.makeText(
                    this@RecommendationCardActivity,
                    "Not Interested",
                    Toast.LENGTH_SHORT
                )
                    .show()
                swipeCard(false)
                recycleView.swipe()
            }

//        CARD ONCLICK LISTENER
            listAdapter.setOnItemClickCallBack(object : CardAdapter.OnItemClickCallBack {
                /*when double click action, current card will be marked as interested*/
                override fun onItemClicked(data: RecommendationDataItem) {
                    Toast.makeText(
                        this@RecommendationCardActivity,
                        "Interested",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    swipeCard(true)
                    binding.recycleView.swipe()
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
                    if (direction != Direction.Bottom) {
                        Toast.makeText(
                            this@RecommendationCardActivity,
                            "Not interested",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                }

                override fun onCardRewound() {}
                override fun onCardCanceled() {}
                override fun onCardAppeared(view: View?, position: Int) {}
                override fun onCardDisappeared(view: View?, position: Int) {
                    if (position == 4) setAnimationsOut()
                }
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

    companion object {

    }
}
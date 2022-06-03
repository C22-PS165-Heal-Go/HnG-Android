package com.example.heal_go.ui.recommendation

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DestinationDetail
import com.example.heal_go.databinding.RecommendationDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailBottomSheet(private val data: DestinationDetail) : BottomSheetDialogFragment() {
    private var binding: RecommendationDetailDialogBinding? = null
    private lateinit var onActionClickListener: OnActionClickListener

    private lateinit var circularProgressDrawable: CircularProgressDrawable

    /*define interface when action is clicked*/
    interface OnActionClickListener {
        fun onActionClicked(actions: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecommendationDetailDialogBinding.inflate(inflater, container, false)
        circularProgressDrawable = context?.let { CircularProgressDrawable(it) }!!
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        circularProgressDrawable.strokeWidth = 8f
        circularProgressDrawable.centerRadius = 40f
        circularProgressDrawable.setColorSchemeColors(R.color.primary_500)
        circularProgressDrawable.start()

        binding?.apply {
            tvTitle.text = data.name
            tvLocation.text = data.location
            tvDescription.text = data.description

            Glide.with(view.context)
                .load(data.imageUrl)
                .apply(
                    RequestOptions.placeholderOf(circularProgressDrawable)
                        .error(R.drawable.image_error_state)
                )
                .into(ivDestination)
        }


        /*when not interested actions is clicked, this will be return not-interested actions to parent page*/
        binding?.notInterestedActions?.setOnClickListener {
            onActionClickListener.onActionClicked(2)
            this.dismiss()
        }
        /*when interested actions is clicked, this will be return interested actions to parent page*/
        binding?.interestedActions?.setOnClickListener {
            onActionClickListener.onActionClicked(1)
            this.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        /*initialize action click listener from parent fragment*/
        try {
            this.onActionClickListener = activity as OnActionClickListener
        } catch (e: ClassCastException) {
            Log.d(TAG, e.message.toString())
        }
    }

    companion object {
        const val TAG = "DetailBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
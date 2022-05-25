package com.example.heal_go.ui.recommendation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.heal_go.databinding.RecommendationDetailDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class DetailBottomSheet : BottomSheetDialogFragment() {
    private var binding: RecommendationDetailDialogBinding? = null
    private lateinit var onActionClickListener: OnActionClickListener

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
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
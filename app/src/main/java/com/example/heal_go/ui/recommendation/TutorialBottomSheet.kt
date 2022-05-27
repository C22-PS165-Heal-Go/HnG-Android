package com.example.heal_go.ui.recommendation

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.heal_go.databinding.RecommendationTutorialDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TutorialBottomSheet : BottomSheetDialogFragment() {
    private var binding: RecommendationTutorialDialogBinding? = null
    private lateinit var onActionClickListener: OnActionClickListener

    /*define interface when action is clicked*/
    interface OnActionClickListener {
        fun onUnderstandBtnClickListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RecommendationTutorialDialogBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*when not interested actions is clicked, this will be return not-interested actions to parent page*/
        binding?.understandBtn?.setOnClickListener {
            onActionClickListener.onUnderstandBtnClickListener()
            this.dismiss()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        /*initialize action click listener from parent fragment*/
        try {
            this.onActionClickListener = activity as OnActionClickListener
        } catch (e: ClassCastException) {
            Log.d(TutorialBottomSheet.TAG, e.message.toString())
        }
    }

    companion object {
        const val TAG = "TutorialBottomSheet"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
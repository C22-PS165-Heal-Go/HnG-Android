package com.example.heal_go.ui.recommendation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.heal_go.databinding.RecommendationTutorialDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TutorialBottomSheet : BottomSheetDialogFragment() {
    private var binding: RecommendationTutorialDialogBinding? = null

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
            this.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
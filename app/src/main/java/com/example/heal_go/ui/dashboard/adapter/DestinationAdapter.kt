package com.example.heal_go.ui.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.heal_go.data.network.response.DestinationItem
import com.example.heal_go.databinding.DestinationCardLayoutBinding

class DestinationAdapter(
    private var destinations: ArrayList<DestinationItem>,
    private val isOnHome: Boolean
) : RecyclerView.Adapter<DestinationAdapter.CardViewHolder>() {
    inner class CardViewHolder(private val binding: DestinationCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DestinationItem) {
            with(binding) {
                val params = itemView.layoutParams

                if (isOnHome) params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                else params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT

                itemView.layoutParams = params

                Glide.with(itemView.context)
                    .load(data.image)
                    .into(imgDestination)

                txtDestinationName.text = data.name
                txtLocation.text = data.location
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            DestinationCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(destinations[holder.adapterPosition])
    }

    override fun getItemCount(): Int = destinations.size
}
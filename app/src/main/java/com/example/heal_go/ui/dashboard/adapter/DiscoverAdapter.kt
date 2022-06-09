package com.example.heal_go.ui.dashboard.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DiscoverItem
import com.example.heal_go.data.network.response.HomeOrDiscoverDestinationData
import com.example.heal_go.databinding.DestinationCardLayoutDiscoverBinding
import com.example.heal_go.ui.detail.DestinationDetailActivity

class DiscoverAdapter :
    PagingDataAdapter<DiscoverItem, DiscoverAdapter.CardViewHolder>(DIFF_CALLBACK) {
    private lateinit var circularProgressDrawable: CircularProgressDrawable

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            DestinationCardLayoutDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        circularProgressDrawable = CircularProgressDrawable(parent.context)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) holder.bind(data)
    }

    inner class CardViewHolder(private val binding: DestinationCardLayoutDiscoverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DiscoverItem) {
            with(binding) {
                val params = itemView.layoutParams
                params.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                itemView.layoutParams = params

                circularProgressDrawable.strokeWidth = 6f
                circularProgressDrawable.centerRadius = 20f
                circularProgressDrawable.setColorSchemeColors(R.color.primary_500)
                circularProgressDrawable.start()

                Glide.with(itemView.context)
                    .load(data.image)
                    .apply(
                        RequestOptions.placeholderOf(circularProgressDrawable)
                            .error(R.drawable.image_error_state)
                    )
                    .into(imgDestination)

                txtDestinationName.text = data.name
                txtLocation.text = data.location

                itemView.setOnClickListener {

                    val intent = Intent(itemView.context, DestinationDetailActivity::class.java)
                    intent.putExtra(DestinationAdapter.DESTINATION_DETAIL, HomeOrDiscoverDestinationData(data, null))

                    itemView.context.startActivity(intent)

                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DiscoverItem>() {
            override fun areItemsTheSame(oldItem: DiscoverItem, newItem: DiscoverItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DiscoverItem, newItem: DiscoverItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
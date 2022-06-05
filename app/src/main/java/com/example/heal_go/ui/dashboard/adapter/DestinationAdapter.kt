package com.example.heal_go.ui.dashboard.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DestinationItem
import com.example.heal_go.data.network.response.HomeOrDiscoverDestinationData
import com.example.heal_go.databinding.DestinationCardLayoutBinding
import com.example.heal_go.ui.detail.DestinationDetailActivity

class DestinationAdapter(
    private var destinations: ArrayList<DestinationItem>
) : RecyclerView.Adapter<DestinationAdapter.CardViewHolder>() {
    private lateinit var circularProgressDrawable: CircularProgressDrawable

    inner class CardViewHolder(private val binding: DestinationCardLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DestinationItem) {
            with(binding) {
                val params = itemView.layoutParams
                params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                itemView.layoutParams = params

                circularProgressDrawable.strokeWidth = 8f
                circularProgressDrawable.centerRadius = 40f
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
                    intent.putExtra(DESTINATION_DETAIL, HomeOrDiscoverDestinationData(null, data))

                    itemView.context.startActivity(intent)

                }

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view =
            DestinationCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        circularProgressDrawable = CircularProgressDrawable(parent.context)
        return CardViewHolder(view)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(destinations[holder.adapterPosition])
    }

    override fun getItemCount(): Int = destinations.size

    companion object {
        const val DESTINATION_DETAIL = "destination_detail"
    }
}
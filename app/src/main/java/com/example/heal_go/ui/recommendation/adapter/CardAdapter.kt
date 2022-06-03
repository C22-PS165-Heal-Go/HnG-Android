package com.example.heal_go.ui.recommendation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.heal_go.R
import com.example.heal_go.data.network.response.DestinationDetail
import com.example.heal_go.data.network.response.RecommendationDataItem
import com.example.heal_go.databinding.CardItemBinding
import com.example.heal_go.util.DoubleClickListener

class CardAdapter(private val listData: ArrayList<RecommendationDataItem>) :
    RecyclerView.Adapter<CardAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallBack
    private lateinit var circularProgressDrawable: CircularProgressDrawable

    /*define click interface*/
    interface OnItemClickCallBack {
        fun onItemClicked(data: RecommendationDataItem)
        fun onHoldClicked(data: DestinationDetail)
    }

    /*set the item click listener*/
    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        circularProgressDrawable = CircularProgressDrawable(parent.context)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[holder.adapterPosition])
    }

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(private val binding: CardItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        /*binding all content in this page*/
        fun bind(data: RecommendationDataItem) {
            with(binding) {

                val destinationDetail = data.id?.let {
                    data.image?.let { it1 ->
                        data.name?.let { it2 ->
                            data.location?.let { it3 ->
                                data.description?.let { it4 ->
                                    DestinationDetail(
                                        it,
                                        it1,
                                        it2,
                                        it3,
                                        it4
                                    )
                                }
                            }
                        }
                    }
                }


                circularProgressDrawable.strokeWidth = 8f
                circularProgressDrawable.centerRadius = 40f
                circularProgressDrawable.setColorSchemeColors(R.color.primary_500)
                circularProgressDrawable.start()

                /*load image*/
                Glide.with(itemView.context)
                    .load(data.image)
                    .apply(
                        RequestOptions.placeholderOf(circularProgressDrawable)
                            .error(R.drawable.image_error_state)
                    )
                    .transform(RoundedCorners(15))
                    .into(cardImage)

                titleCard.text = data.name
                locationCard.text = data.location

                /*define item click listener, even when long click too*/
                itemView.setOnClickListener(object : DoubleClickListener() {
                    /*this listener can run when user do double click to card*/
                    override fun onDoubleClick(v: View) {
                        onItemClickCallBack.onItemClicked(data)
                    }
                })

                itemView.setOnLongClickListener {
                    /*this will be defined hold function from listener*/
                    if (destinationDetail != null) {
                        onItemClickCallBack.onHoldClicked(destinationDetail)
                    }
                    true
                }
            }
        }
    }

}
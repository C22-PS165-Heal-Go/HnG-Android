package com.example.heal_go.ui.recommendation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.heal_go.R
import com.example.heal_go.databinding.CardItemBinding
import com.example.heal_go.ui.recommendation.DetailBottomSheet
import com.example.heal_go.util.DoubleClickListener

class CardAdapter(private val listData: ArrayList<String>) :
    RecyclerView.Adapter<CardAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallBack: OnItemClickCallBack

    /*define click interface*/
    interface OnItemClickCallBack {
        fun onItemClicked(data: String)
        fun onHoldClicked()
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
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listData[holder.adapterPosition])
    }

    override fun getItemCount(): Int = listData.size

    inner class ListViewHolder(private val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        /*binding all content in this page*/
        fun bind(data: String) {
            with(binding) {
                /*load image*/
                Glide.with(itemView.context)
                    .load(R.drawable.dewabujana)
                    .transform(RoundedCorners(15))
                    .into(cardImage)

                /*define item click listener, even when long click too*/
                itemView.setOnClickListener(object : DoubleClickListener() {
                    /*this listener can run when user do double click to card*/
                    override fun onDoubleClick(v: View) {
                        onItemClickCallBack.onItemClicked(data)
                    }
                })

                itemView.setOnLongClickListener {
                    /*this will be defined hold function from listener*/
                    onItemClickCallBack.onHoldClicked()
                    true
                }
            }
        }
    }

}
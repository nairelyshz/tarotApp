package com.nh.tarotapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card.view.*
import kotlinx.android.synthetic.main.card.view.image
import kotlinx.android.synthetic.main.card_details.view.*

class CardDetailsAdapter(private val cards: List<Card>): RecyclerView.Adapter<CardDetailsAdapter.ViewHolder>(){


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        fun bind(item: Card ) {
            itemView.image.setImageDrawable(item.image)
            itemView.description.text = item.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardDetailsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_details,parent,false)
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cards[position])

    }

    override fun getItemCount(): Int {
        return cards.size;
    }
}